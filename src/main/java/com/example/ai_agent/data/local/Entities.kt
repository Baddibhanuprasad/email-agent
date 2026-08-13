package com.example.ai_agent.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "processed_emails")
data class ProcessedEmailEntity(
    @PrimaryKey val emailId: String,
    val threadId: String,
    val subject: String,
    val sender: String,
    val priority: String,
    val summary: String,
    val actionItemsJson: String,
    val receivedAt: Long,
    val processedAt: Long,
    val requiresReply: Boolean
)

@Entity(tableName = "drafts")
data class DraftEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val emailId: String,
    val threadId: String,
    val subject: String,
    val recipient: String,
    val body: String,
    val createdAt: Long,
    val pushedToGmail: Boolean = false,
    val gmailDraftId: String? = null
)

@Entity(tableName = "meeting_reminders")
data class MeetingReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventId: String,
    val title: String,
    val startTime: Long,
    val reminderType: String,
    val scheduledAt: Long,
    val fired: Boolean = false,
    val source: String
)

@Entity(tableName = "sync_state")
data class SyncStateEntity(
    @PrimaryKey val id: Int = 1,
    val lastSyncAt: Long? = null,
    val lastHistoryId: String? = null,
    val lastError: String? = null,
    val emailsProcessed: Int = 0
)
