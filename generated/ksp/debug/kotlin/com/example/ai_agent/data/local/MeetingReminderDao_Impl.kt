package com.example.ai_agent.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MeetingReminderDao_Impl(
  __db: RoomDatabase,
) : MeetingReminderDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMeetingReminderEntity: EntityInsertAdapter<MeetingReminderEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfMeetingReminderEntity = object :
        EntityInsertAdapter<MeetingReminderEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `meeting_reminders` (`id`,`eventId`,`title`,`startTime`,`reminderType`,`scheduledAt`,`fired`,`source`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MeetingReminderEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.eventId)
        statement.bindText(3, entity.title)
        statement.bindLong(4, entity.startTime)
        statement.bindText(5, entity.reminderType)
        statement.bindLong(6, entity.scheduledAt)
        val _tmp: Int = if (entity.fired) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindText(8, entity.source)
      }
    }
  }

  public override suspend fun insertAll(entities: List<MeetingReminderEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMeetingReminderEntity.insert(_connection, entities)
  }

  public override suspend fun getDueReminders(now: Long): List<MeetingReminderEntity> {
    val _sql: String = "SELECT * FROM meeting_reminders WHERE fired = 0 AND scheduledAt <= ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfEventId: Int = getColumnIndexOrThrow(_stmt, "eventId")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _columnIndexOfReminderType: Int = getColumnIndexOrThrow(_stmt, "reminderType")
        val _columnIndexOfScheduledAt: Int = getColumnIndexOrThrow(_stmt, "scheduledAt")
        val _columnIndexOfFired: Int = getColumnIndexOrThrow(_stmt, "fired")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _result: MutableList<MeetingReminderEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: MeetingReminderEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpEventId: String
          _tmpEventId = _stmt.getText(_columnIndexOfEventId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpStartTime: Long
          _tmpStartTime = _stmt.getLong(_columnIndexOfStartTime)
          val _tmpReminderType: String
          _tmpReminderType = _stmt.getText(_columnIndexOfReminderType)
          val _tmpScheduledAt: Long
          _tmpScheduledAt = _stmt.getLong(_columnIndexOfScheduledAt)
          val _tmpFired: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfFired).toInt()
          _tmpFired = _tmp != 0
          val _tmpSource: String
          _tmpSource = _stmt.getText(_columnIndexOfSource)
          _item =
              MeetingReminderEntity(_tmpId,_tmpEventId,_tmpTitle,_tmpStartTime,_tmpReminderType,_tmpScheduledAt,_tmpFired,_tmpSource)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markFired(id: Long) {
    val _sql: String = "UPDATE meeting_reminders SET fired = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteForEvent(eventId: String) {
    val _sql: String = "DELETE FROM meeting_reminders WHERE eventId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, eventId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
