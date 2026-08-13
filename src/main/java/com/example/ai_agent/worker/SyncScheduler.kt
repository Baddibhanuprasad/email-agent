package com.example.ai_agent.worker

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object SyncScheduler {

    private const val SYNC_WORK = "email_sync_chain"
    private const val REMINDER_WORK = "meeting_reminder_chain"
    private const val SYNC_INTERVAL_MINUTES = 2L

    fun schedulePeriodicSync(context: Context) {
        enqueueSync(context, initialDelayMinutes = 1)
        enqueueReminderCheck(context, initialDelayMinutes = 2)
    }

    fun enqueueSync(context: Context, initialDelayMinutes: Long = 0) {
        val request = OneTimeWorkRequestBuilder<EmailSyncWorker>()
            .setInitialDelay(initialDelayMinutes, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            SYNC_WORK,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun enqueueReminderCheck(context: Context, initialDelayMinutes: Long = 0) {
        val request = OneTimeWorkRequestBuilder<MeetingReminderWorker>()
            .setInitialDelay(initialDelayMinutes, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            REMINDER_WORK,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun rescheduleSync(context: Context) {
        enqueueSync(context, initialDelayMinutes = SYNC_INTERVAL_MINUTES)
    }

    fun rescheduleReminders(context: Context) {
        enqueueReminderCheck(context, initialDelayMinutes = 15)
    }
}
