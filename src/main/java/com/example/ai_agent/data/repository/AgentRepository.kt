package com.example.ai_agent.data.repository

import android.content.Context
import com.example.ai_agent.data.ai.GeminiAnalyzer
import com.example.ai_agent.data.ai.GeminiKeyManager
import com.example.ai_agent.data.google.CalendarClient
import com.example.ai_agent.data.google.GmailClient
import com.example.ai_agent.data.google.GoogleAuthManager
import com.example.ai_agent.data.google.TasksClient
import com.example.ai_agent.data.local.AppDatabase
import com.example.ai_agent.data.local.DraftEntity
import com.example.ai_agent.data.local.MeetingReminderEntity
import com.example.ai_agent.data.local.ProcessedEmailEntity
import com.example.ai_agent.data.local.SecureKeyStore
import com.example.ai_agent.data.local.SettingsStore
import com.example.ai_agent.data.local.SyncStateEntity
import com.example.ai_agent.data.model.DraftPreview
import com.example.ai_agent.data.model.MeetingInfo
import com.example.ai_agent.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.Calendar

class AgentRepository(
    private val context: Context,
    private val database: AppDatabase,
    private val secureKeyStore: SecureKeyStore,
    val settingsStore: SettingsStore,
    private val googleAuthManager: GoogleAuthManager
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val geminiKeyManager = GeminiKeyManager(secureKeyStore)
    private val analyzer = GeminiAnalyzer(geminiKeyManager)

    val processedEmails: Flow<List<ProcessedEmailEntity>> =
        database.processedEmailDao().observeTopPrioritized()

    val drafts: Flow<List<DraftPreview>> =
        database.draftDao().observeAll().map { list ->
            list.map { entity ->
                DraftPreview(
                    id = entity.id,
                    emailId = entity.emailId,
                    threadId = entity.threadId,
                    subject = entity.subject,
                    recipient = entity.recipient,
                    body = entity.body,
                    createdAt = entity.createdAt,
                    pushedToGmail = entity.pushedToGmail
                )
            }
        }

    val syncState = database.syncStateDao().observe()

    fun getGeminiKeys(): List<String> = secureKeyStore.getGeminiKeys()

    fun addGeminiKey(key: String) = secureKeyStore.addGeminiKey(key)

    fun removeGeminiKey(key: String) = secureKeyStore.removeGeminiKey(key)

    suspend fun runSync() = withContext(Dispatchers.IO) {
        if (!googleAuthManager.isSignedIn()) {
            updateSyncError("Sign in with Google first.")
            return@withContext
        }
        if (geminiKeyManager.getKeys().isEmpty()) {
            updateSyncError("Add at least one Gemini API key in Settings.")
            return@withContext
        }

        val gmailService = googleAuthManager.gmail() ?: run {
            updateSyncError("Gmail access unavailable.")
            return@withContext
        }
        val tasksService = googleAuthManager.tasks()
        val calendarService = googleAuthManager.calendar()

        val gmail = GmailClient(gmailService)
        val tasks = tasksService?.let { TasksClient(it) }
        val calendar = calendarService?.let { CalendarClient(it) }

        try {
            val processedIds = database.processedEmailDao().getAllIds().toSet()
            val emails = gmail.fetchRecentEmails(processedIds = processedIds)
            var count = 0

            for (email in emails) {
                try {
                    val analyzed = analyzer.analyze(email)
                    database.processedEmailDao().insert(
                        ProcessedEmailEntity(
                            emailId = email.id,
                            threadId = email.threadId,
                            subject = email.subject,
                            sender = email.sender,
                            priority = analyzed.priority.name,
                            summary = analyzed.summary,
                            actionItemsJson = json.encodeToString(
                                ListSerializer(String.serializer()),
                                analyzed.actionItems
                            ),
                            receivedAt = email.receivedAt,
                            processedAt = System.currentTimeMillis(),
                            requiresReply = analyzed.requiresReply
                        )
                    )

                    runCatching { tasks?.addTasks(analyzed.actionItems, email.subject) }

                    analyzed.meetingDetected?.let { meeting ->
                        runCatching { calendar?.createEventFromMeeting(meeting) }
                        runCatching { scheduleMeetingReminders(meeting, email.id) }
                    }

                    if (analyzed.requiresReply && !analyzed.suggestedReply.isNullOrBlank()) {
                        runCatching {
                            database.draftDao().insert(
                                DraftEntity(
                                    emailId = email.id,
                                    threadId = email.threadId,
                                    subject = "Re: ${email.subject}",
                                    recipient = extractEmailAddress(email.sender),
                                    body = analyzed.suggestedReply,
                                    createdAt = System.currentTimeMillis()
                                )
                            )
                        }
                    }
                    count++
                } catch (emailErr: Exception) {
                    // Log individual email processing error so remaining emails continue
                }
            }

            calendar?.fetchUpcomingEvents()?.forEach { meeting ->
                scheduleMeetingReminders(meeting, meeting.title)
            }

            database.syncStateDao().upsert(
                SyncStateEntity(
                    lastSyncAt = System.currentTimeMillis(),
                    lastError = null,
                    emailsProcessed = (database.syncStateDao().get()?.emailsProcessed ?: 0) + count
                )
            )
        } catch (e: Exception) {
            updateSyncError(e.message ?: "Sync failed")
        }
    }

    suspend fun updateDraft(id: Long, body: String) = withContext(Dispatchers.IO) {
        val draft = database.draftDao().getById(id) ?: return@withContext
        database.draftDao().update(draft.copy(body = body))
    }

    suspend fun pushDraftToGmail(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val draft = database.draftDao().getById(id)
                ?: error("Draft not found")
            val gmail = googleAuthManager.gmail() ?: error("Not signed in")
            val client = GmailClient(gmail)
            val gmailDraftId = client.createDraft(
                threadId = draft.threadId,
                to = draft.recipient,
                subject = draft.subject,
                body = draft.body,
                inReplyToMessageId = draft.emailId
            )
            database.draftDao().update(
                draft.copy(pushedToGmail = true, gmailDraftId = gmailDraftId)
            )
        }
    }

    suspend fun fireDueReminders() = withContext(Dispatchers.IO) {
        val due = database.meetingReminderDao().getDueReminders(System.currentTimeMillis())
        for (reminder in due) {
            NotificationHelper.showMeetingReminder(
                context = context,
                title = reminder.title,
                reminderType = reminder.reminderType,
                startTime = reminder.startTime
            )
            database.meetingReminderDao().markFired(reminder.id)
        }
    }

    private suspend fun scheduleMeetingReminders(meeting: MeetingInfo, eventKey: String) {
        val startMillis = runCatching { Instant.parse(meeting.startTimeIso).toEpochMilli() }
            .getOrNull() ?: return

        val reminders = listOf(
            ReminderSpec("1_day_before", startMillis - ONE_DAY_MS),
            ReminderSpec("3_hours_before", startMillis - THREE_HOURS_MS)
        )

        val entities = reminders
            .filter { it.scheduledAt > System.currentTimeMillis() }
            .map { spec ->
                MeetingReminderEntity(
                    eventId = "$eventKey-${spec.type}",
                    title = meeting.title,
                    startTime = startMillis,
                    reminderType = spec.type,
                    scheduledAt = spec.scheduledAt,
                    source = meeting.source
                )
            }

        if (entities.isNotEmpty()) {
            database.meetingReminderDao().insertAll(entities)
        }
    }

    private suspend fun updateSyncError(message: String) {
        val current = database.syncStateDao().get()
        database.syncStateDao().upsert(
            SyncStateEntity(
                lastSyncAt = current?.lastSyncAt,
                lastHistoryId = current?.lastHistoryId,
                lastError = message,
                emailsProcessed = current?.emailsProcessed ?: 0
            )
        )
    }

    private fun extractEmailAddress(from: String): String {
        val match = Regex("<([^>]+)>").find(from)
        return match?.groupValues?.get(1) ?: from.trim()
    }

    private fun currentHour(): Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    private data class ReminderSpec(val type: String, val scheduledAt: Long)

    private companion object {
        const val ONE_DAY_MS = 24L * 60 * 60 * 1000
        const val THREE_HOURS_MS = 3L * 60 * 60 * 1000
    }
}
