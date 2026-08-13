package com.example.ai_agent.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class EmailPriority(val label: String) {
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

data class EmailSummary(
    val id: String,
    val threadId: String,
    val subject: String,
    val sender: String,
    val snippet: String,
    val receivedAt: Long,
    val isRead: Boolean,
    val bodyPreview: String
)

data class AnalyzedEmail(
    val email: EmailSummary,
    val priority: EmailPriority,
    val summary: String,
    val actionItems: List<String>,
    val suggestedReply: String?,
    val meetingDetected: MeetingInfo?,
    val requiresReply: Boolean
)

@Serializable
data class MeetingInfo(
    val title: String,
    val startTimeIso: String,
    val endTimeIso: String?,
    val location: String?,
    val source: String
)

data class DraftPreview(
    val id: Long,
    val emailId: String,
    val threadId: String,
    val subject: String,
    val recipient: String,
    val body: String,
    val createdAt: Long,
    val pushedToGmail: Boolean
)

data class SyncStatus(
    val lastSyncAt: Long?,
    val lastError: String?,
    val emailsProcessed: Int,
    val isRunning: Boolean
)
