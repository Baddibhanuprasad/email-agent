package com.example.ai_agent.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SecureKeyStore(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "secure_keys",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getGeminiKeys(): List<String> =
        prefs.getStringSet(KEY_GEMINI_KEYS, emptySet())?.toList().orEmpty()

    fun addGeminiKey(key: String) {
        val updated = getGeminiKeys().toMutableSet()
        updated.add(key.trim())
        prefs.edit().putStringSet(KEY_GEMINI_KEYS, updated).apply()
    }

    fun removeGeminiKey(key: String) {
        val updated = getGeminiKeys().toMutableSet()
        updated.remove(key)
        prefs.edit().putStringSet(KEY_GEMINI_KEYS, updated).apply()
    }

    private companion object {
        const val KEY_GEMINI_KEYS = "gemini_api_keys"
    }
}
