package com.example.ai_agent.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProcessedEmailEntity::class,
        DraftEntity::class,
        MeetingReminderEntity::class,
        SyncStateEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun processedEmailDao(): ProcessedEmailDao
    abstract fun draftDao(): DraftDao
    abstract fun meetingReminderDao(): MeetingReminderDao
    abstract fun syncStateDao(): SyncStateDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "ai_agent.db").build()
    }
}
