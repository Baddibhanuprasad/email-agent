package com.example.ai_agent.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.ai_agent.AiAgentApplication

class MeetingReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as AiAgentApplication
        app.agentRepository.fireDueReminders()
        SyncScheduler.rescheduleReminders(applicationContext)
        return Result.success()
    }
}
