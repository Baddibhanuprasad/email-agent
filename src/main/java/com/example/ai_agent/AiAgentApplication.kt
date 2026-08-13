package com.example.ai_agent

import android.app.Application
import com.example.ai_agent.data.google.GoogleAuthManager
import com.example.ai_agent.data.local.AppDatabase
import com.example.ai_agent.data.local.SecureKeyStore
import com.example.ai_agent.data.local.SettingsStore
import com.example.ai_agent.data.repository.AgentRepository
import com.example.ai_agent.notification.NotificationHelper
import com.example.ai_agent.worker.SyncScheduler

class AiAgentApplication : Application() {

    lateinit var database: AppDatabase
        private set
    lateinit var secureKeyStore: SecureKeyStore
        private set
    lateinit var settingsStore: SettingsStore
        private set
    lateinit var googleAuthManager: GoogleAuthManager
        private set
    lateinit var agentRepository: AgentRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.create(this)
        secureKeyStore = SecureKeyStore(this)
        settingsStore = SettingsStore(this)
        googleAuthManager = GoogleAuthManager(this)
        agentRepository = AgentRepository(
            context = this,
            database = database,
            secureKeyStore = secureKeyStore,
            settingsStore = settingsStore,
            googleAuthManager = googleAuthManager
        )
        NotificationHelper.createChannels(this)
        SyncScheduler.schedulePeriodicSync(this)
    }

    companion object {
        lateinit var instance: AiAgentApplication
            private set
    }
}
