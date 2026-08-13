package com.example.ai_agent.data.ai

import com.example.ai_agent.data.local.SecureKeyStore
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import java.util.concurrent.atomic.AtomicInteger

class GeminiKeyManager(private val secureKeyStore: SecureKeyStore) {

    private val index = AtomicInteger(0)

    fun getKeys(): List<String> = secureKeyStore.getGeminiKeys()

    fun nextModel(): GenerativeModel? {
        val keys = getKeys()
        if (keys.isEmpty()) return null
        val start = index.getAndIncrement()
        for (offset in keys.indices) {
            val key = keys[(start + offset) % keys.size]
            runCatching {
                return GenerativeModel(
                    modelName = MODEL_NAME,
                    apiKey = key
                )
            }
        }
        return null
    }

    suspend fun <T> withModel(block: suspend (GenerativeModel) -> T): T {
        val keys = getKeys()
        require(keys.isNotEmpty()) { "Add at least one Gemini API key in Settings." }
        val modelsToTry = listOf("gemini-2.5-flash", "gemini-2.0-flash", "gemini-1.5-flash", "gemini-pro")
        var lastError: Exception? = null

        for (modelName in modelsToTry) {
            for (offset in keys.indices) {
                val key = keys[(index.getAndIncrement() + offset) % keys.size]
                try {
                    val model = GenerativeModel(modelName = modelName, apiKey = key)
                    return block(model)
                } catch (e: Exception) {
                    lastError = e
                }
            }
        }
        val rawMsg = lastError?.message.orEmpty()
        val cleanMsg = when {
            rawMsg.contains("429") || rawMsg.contains("quota", ignoreCase = true) || rawMsg.contains("RESOURCE_EXHAUSTED", ignoreCase = true) ->
                "Gemini API Quota Exceeded (429 Rate Limit). Please wait a minute or create a fresh API key at aistudio.google.com."
            rawMsg.contains("not found", ignoreCase = true) ->
                "Gemini model not found. Please verify your API key."
            else -> rawMsg.take(150).ifBlank { "Gemini API request failed." }
        }
        throw IllegalStateException(cleanMsg)
    }

    companion object {
        const val MODEL_NAME = "gemini-2.5-flash"
    }
}
