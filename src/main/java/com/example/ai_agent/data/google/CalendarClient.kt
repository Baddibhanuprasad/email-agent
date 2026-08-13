package com.example.ai_agent.data.google

import com.example.ai_agent.data.model.MeetingInfo
import com.google.api.services.calendar.Calendar
import java.time.Instant
import java.time.format.DateTimeFormatter

class CalendarClient(private val calendar: Calendar) {

    fun fetchUpcomingEvents(daysAhead: Int = 14): List<MeetingInfo> {
        val now = Instant.now()
        val end = now.plusSeconds(daysAhead * 24L * 3600)
        val events = calendar.events().list("primary")
            .setTimeMin(com.google.api.client.util.DateTime(now.toEpochMilli()))
            .setTimeMax(com.google.api.client.util.DateTime(end.toEpochMilli()))
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute()
            .items.orEmpty()

        return events.mapNotNull { event ->
            val start = event.start?.dateTime ?: event.start?.date ?: return@mapNotNull null
            val endTime = event.end?.dateTime ?: event.end?.date
            MeetingInfo(
                title = event.summary ?: "Meeting",
                startTimeIso = formatDateTime(start),
                endTimeIso = endTime?.let { formatDateTime(it) },
                location = event.location,
                source = "calendar"
            )
        }
    }

    suspend fun createEventFromMeeting(meeting: MeetingInfo): String {
        val startInstant = Instant.parse(meeting.startTimeIso)
        val endInstant = meeting.endTimeIso?.let { Instant.parse(it) }
            ?: startInstant.plusSeconds(3600)

        val event = com.google.api.services.calendar.model.Event().apply {
            summary = meeting.title
            location = meeting.location
            start = com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(com.google.api.client.util.DateTime(startInstant.toEpochMilli()))
            end = com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(com.google.api.client.util.DateTime(endInstant.toEpochMilli()))
        }
        return calendar.events().insert("primary", event).execute().id
    }

    private fun formatDateTime(value: com.google.api.client.util.DateTime): String {
        return Instant.ofEpochMilli(value.value).toString()
    }
}
