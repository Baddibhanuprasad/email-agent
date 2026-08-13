package com.example.ai_agent.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcessedEmailDao {
    @Query("""
        SELECT * FROM processed_emails 
        ORDER BY 
            CASE priority 
                WHEN 'CRITICAL' THEN 0 
                WHEN 'HIGH' THEN 1 
                WHEN 'MEDIUM' THEN 2 
                WHEN 'LOW' THEN 3 
                ELSE 4 
            END ASC, 
            receivedAt DESC
    """)
    fun observeAll(): Flow<List<ProcessedEmailEntity>>

    @Query("""
        SELECT * FROM processed_emails 
        ORDER BY 
            CASE priority 
                WHEN 'CRITICAL' THEN 0 
                WHEN 'HIGH' THEN 1 
                WHEN 'MEDIUM' THEN 2 
                WHEN 'LOW' THEN 3 
                ELSE 4 
            END ASC, 
            receivedAt DESC 
        LIMIT 10
    """)
    fun observeTopPrioritized(): Flow<List<ProcessedEmailEntity>>

    @Query("SELECT emailId FROM processed_emails")
    suspend fun getAllIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ProcessedEmailEntity)

    @Query("SELECT * FROM processed_emails WHERE emailId = :emailId LIMIT 1")
    suspend fun getById(emailId: String): ProcessedEmailEntity?
}

@Dao
interface DraftDao {
    @Query("SELECT * FROM drafts ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<DraftEntity>>

    @Query("SELECT * FROM drafts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): DraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DraftEntity): Long

    @Update
    suspend fun update(entity: DraftEntity)
}

@Dao
interface MeetingReminderDao {
    @Query("SELECT * FROM meeting_reminders WHERE fired = 0 AND scheduledAt <= :now")
    suspend fun getDueReminders(now: Long): List<MeetingReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<MeetingReminderEntity>)

    @Query("UPDATE meeting_reminders SET fired = 1 WHERE id = :id")
    suspend fun markFired(id: Long)

    @Query("DELETE FROM meeting_reminders WHERE eventId = :eventId")
    suspend fun deleteForEvent(eventId: String)
}

@Dao
interface SyncStateDao {
    @Query("SELECT * FROM sync_state WHERE id = 1 LIMIT 1")
    fun observe(): Flow<SyncStateEntity?>

    @Query("SELECT * FROM sync_state WHERE id = 1 LIMIT 1")
    suspend fun get(): SyncStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SyncStateEntity)
}
