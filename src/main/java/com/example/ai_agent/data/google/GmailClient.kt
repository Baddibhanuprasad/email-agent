package com.example.ai_agent.data.google

import android.util.Base64
import com.example.ai_agent.data.model.EmailSummary
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Draft
import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePart
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GmailClient(private val gmail: Gmail) {

    suspend fun fetchRecentEmails(maxResults: Int = 50, processedIds: Set<String>): List<EmailSummary> {
        val listResponse = gmail.users().messages().list("me")
            .setMaxResults(maxResults.toLong())
            .execute()

        return listResponse.messages.orEmpty()
            .mapNotNull { ref -> ref.id }
            .filter { it !in processedIds }
            .mapNotNull { id ->
                runCatching { fetchMessage(id) }.getOrNull()
            }
    }

    private fun fetchMessage(id: String): EmailSummary {
        val message = gmail.users().messages().get("me", id)
            .setFormat("full")
            .execute()

        val headers = message.payload?.headers.orEmpty()
        val subject = headers.find { it.name.equals("Subject", true) }?.value.orEmpty()
        val from = headers.find { it.name.equals("From", true) }?.value.orEmpty()
        val dateHeader = headers.find { it.name.equals("Date", true) }?.value
        val receivedAt = parseDate(dateHeader) ?: (message.internalDate ?: System.currentTimeMillis())
        val body = extractBody(message.payload)

        return EmailSummary(
            id = message.id,
            threadId = message.threadId,
            subject = subject.ifBlank { "(No subject)" },
            sender = from,
            snippet = message.snippet.orEmpty(),
            receivedAt = receivedAt,
            isRead = message.labelIds?.contains("UNREAD") != true,
            bodyPreview = body.take(4000)
        )
    }

    fun createDraft(
        threadId: String,
        to: String,
        subject: String,
        body: String,
        inReplyToMessageId: String?
    ): String {
        val raw = buildRawEmail(to, subject, body, inReplyToMessageId)
        val message = Message().apply {
            this.raw = raw
            this.threadId = threadId
        }
        val draft = Draft().setMessage(message)
        return gmail.users().drafts().create("me", draft).execute().id
    }

    private fun buildRawEmail(
        to: String,
        subject: String,
        body: String,
        inReplyToMessageId: String?
    ): String {
        val replyHeaders = if (inReplyToMessageId != null) {
            "In-Reply-To: <$inReplyToMessageId>\r\nReferences: <$inReplyToMessageId>\r\n"
        } else ""
        val email = buildString {
            append("To: $to\r\n")
            append("Subject: $subject\r\n")
            append(replyHeaders)
            append("Content-Type: text/plain; charset=UTF-8\r\n")
            append("\r\n")
            append(body)
        }
        return Base64.encodeToString(
            email.toByteArray(StandardCharsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_WRAP
        )
    }

    private fun extractBody(part: MessagePart?): String {
        if (part == null) return ""
        part.body?.data?.let { data ->
            return decodeBase64(data)
        }
        part.parts?.forEach { child ->
            val text = extractBody(child)
            if (text.isNotBlank()) return text
        }
        return ""
    }

    private fun decodeBase64(data: String): String {
        val decoded = Base64.decode(data, Base64.URL_SAFE)
        return String(decoded, StandardCharsets.UTF_8)
    }

    private fun parseDate(dateHeader: String?): Long? {
        if (dateHeader.isNullOrBlank()) return null
        val formats = listOf(
            "EEE, dd MMM yyyy HH:mm:ss Z",
            "dd MMM yyyy HH:mm:ss Z"
        )
        for (pattern in formats) {
            runCatching {
                return SimpleDateFormat(pattern, Locale.US).parse(dateHeader)?.time
            }
        }
        return null
    }
}
