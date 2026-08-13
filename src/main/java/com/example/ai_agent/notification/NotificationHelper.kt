package com.example.ai_agent.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.DateFormat
import java.util.Date

object NotificationHelper {

    private const val CHANNEL_MEETINGS = "meeting_reminders"

    fun createChannels(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_MEETINGS,
            "Meeting Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Reminders 1 day and 3 hours before meetings"
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun showMeetingReminder(
        context: Context,
        title: String,
        reminderType: String,
        startTime: Long
    ) {
        val whenText = when (reminderType) {
            "1_day_before" -> "Tomorrow"
            "3_hours_before" -> "In 3 hours"
            else -> DateFormat.getDateTimeInstance().format(Date(startTime))
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_MEETINGS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Meeting: $title")
            .setContentText("$whenText — ${DateFormat.getDateTimeInstance().format(Date(startTime))}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify((title + reminderType).hashCode(), notification)
    }
}
