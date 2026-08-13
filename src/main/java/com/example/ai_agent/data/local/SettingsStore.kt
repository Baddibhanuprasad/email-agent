package com.example.ai_agent.data.local

import android.content.Context

class SettingsStore(context: Context) {

    private val prefs = context.getSharedPreferences("agent_settings", Context.MODE_PRIVATE)

    var quietHoursStart: Int
        get() = prefs.getInt(KEY_QUIET_START, 22)
        set(value) = prefs.edit().putInt(KEY_QUIET_START, value).apply()

    var quietHoursEnd: Int
        get() = prefs.getInt(KEY_QUIET_END, 7)
        set(value) = prefs.edit().putInt(KEY_QUIET_END, value).apply()

    var syncEnabled: Boolean
        get() = prefs.getBoolean(KEY_SYNC_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_SYNC_ENABLED, value).apply()

    fun isWithinQuietHours(hourOfDay: Int): Boolean = false

    private companion object {
        const val KEY_QUIET_START = "quiet_hours_start"
        const val KEY_QUIET_END = "quiet_hours_end"
        const val KEY_SYNC_ENABLED = "sync_enabled"
    }
}
