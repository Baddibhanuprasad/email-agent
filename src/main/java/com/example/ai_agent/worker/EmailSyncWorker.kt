package com.example.ai_agent.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ai_agent.AiAgentApplication

class EmailSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as AiAgentApplication
        if (app.settingsStore.syncEnabled) {
            app.agentRepository.runSync()
        }
        SyncScheduler.rescheduleSync(applicationContext)
        return Result.success()
    }
}
