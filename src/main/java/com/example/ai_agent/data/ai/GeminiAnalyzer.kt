package com.example.ai_agent.data.ai

import com.example.ai_agent.data.model.AnalyzedEmail
import com.example.ai_agent.data.model.EmailPriority
import com.example.ai_agent.data.model.EmailSummary
import com.example.ai_agent.data.model.MeetingInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class GeminiAnalyzer(private val keyManager: GeminiKeyManager) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun analyze(email: EmailSummary): AnalyzedEmail {
        val response = keyManager.withModel { model ->
            model.generateContent(buildPrompt(email)).text.orEmpty()
        }
        val parsed = parseResponse(response)
        return AnalyzedEmail(
            email = email,
            priority = parsed.priority,
            summary = parsed.summary,
            actionItems = parsed.actionItems,
            suggestedReply = parsed.suggestedReply,
            meetingDetected = parsed.meeting,
            requiresReply = parsed.requiresReply
        )
    }

    private fun buildPrompt(email: EmailSummary): String = """
        Analyze this email and respond ONLY with valid JSON (no markdown):
        {
          "priority": "CRITICAL|HIGH|MEDIUM|LOW",
          "summary": "one sentence summary",
          "actionItems": ["task1", "task2"],
          "requiresReply": true/false,
          "suggestedReply": "draft reply text or null if no reply needed",
          "meeting": {
            "title": "meeting title",
            "startTimeIso": "2026-08-07T10:00:00Z",
            "endTimeIso": "2026-08-07T11:00:00Z",
            "location": "optional",
            "source": "email"
          } or null
        }

        From: ${email.sender}
        Subject: ${email.subject}
        Body:
        ${email.bodyPreview.ifBlank { email.snippet }}
    """.trimIndent()

    private fun parseResponse(raw: String): ParsedAnalysis {
        val cleaned = raw.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
        return runCatching {
            json.decodeFromString<ParsedAnalysis>(cleaned)
        }.getOrElse {
            ParsedAnalysis(
                priority = EmailPriority.MEDIUM,
                summary = cleaned.take(200),
                actionItems = emptyList(),
                requiresReply = false,
                suggestedReply = null,
                meeting = null
            )
        }
    }

    @Serializable
    private data class ParsedAnalysis(
        val priority: EmailPriority = EmailPriority.MEDIUM,
        val summary: String = "",
        val actionItems: List<String> = emptyList(),
        val requiresReply: Boolean = false,
        val suggestedReply: String? = null,
        val meeting: MeetingInfo? = null
    )
}
