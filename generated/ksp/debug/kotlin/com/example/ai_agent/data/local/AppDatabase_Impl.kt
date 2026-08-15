package com.example.ai_agent.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _processedEmailDao: Lazy<ProcessedEmailDao> = lazy {
    ProcessedEmailDao_Impl(this)
  }

  private val _draftDao: Lazy<DraftDao> = lazy {
    DraftDao_Impl(this)
  }

  private val _meetingReminderDao: Lazy<MeetingReminderDao> = lazy {
    MeetingReminderDao_Impl(this)
  }

  private val _syncStateDao: Lazy<SyncStateDao> = lazy {
    SyncStateDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "7102087e58a0956bb9f1004d8808be72", "1e5eb0993ffb28c45233bc7c9a7f530c") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `processed_emails` (`emailId` TEXT NOT NULL, `threadId` TEXT NOT NULL, `subject` TEXT NOT NULL, `sender` TEXT NOT NULL, `priority` TEXT NOT NULL, `summary` TEXT NOT NULL, `actionItemsJson` TEXT NOT NULL, `receivedAt` INTEGER NOT NULL, `processedAt` INTEGER NOT NULL, `requiresReply` INTEGER NOT NULL, PRIMARY KEY(`emailId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `drafts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `emailId` TEXT NOT NULL, `threadId` TEXT NOT NULL, `subject` TEXT NOT NULL, `recipient` TEXT NOT NULL, `body` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `pushedToGmail` INTEGER NOT NULL, `gmailDraftId` TEXT)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `meeting_reminders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `eventId` TEXT NOT NULL, `title` TEXT NOT NULL, `startTime` INTEGER NOT NULL, `reminderType` TEXT NOT NULL, `scheduledAt` INTEGER NOT NULL, `fired` INTEGER NOT NULL, `source` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sync_state` (`id` INTEGER NOT NULL, `lastSyncAt` INTEGER, `lastHistoryId` TEXT, `lastError` TEXT, `emailsProcessed` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7102087e58a0956bb9f1004d8808be72')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `processed_emails`")
        connection.execSQL("DROP TABLE IF EXISTS `drafts`")
        connection.execSQL("DROP TABLE IF EXISTS `meeting_reminders`")
        connection.execSQL("DROP TABLE IF EXISTS `sync_state`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsProcessedEmails: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProcessedEmails.put("emailId", TableInfo.Column("emailId", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("threadId", TableInfo.Column("threadId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("subject", TableInfo.Column("subject", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("sender", TableInfo.Column("sender", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("priority", TableInfo.Column("priority", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("summary", TableInfo.Column("summary", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("actionItemsJson", TableInfo.Column("actionItemsJson", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("receivedAt", TableInfo.Column("receivedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("processedAt", TableInfo.Column("processedAt", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProcessedEmails.put("requiresReply", TableInfo.Column("requiresReply", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProcessedEmails: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProcessedEmails: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoProcessedEmails: TableInfo = TableInfo("processed_emails", _columnsProcessedEmails,
            _foreignKeysProcessedEmails, _indicesProcessedEmails)
        val _existingProcessedEmails: TableInfo = read(connection, "processed_emails")
        if (!_infoProcessedEmails.equals(_existingProcessedEmails)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |processed_emails(com.example.ai_agent.data.local.ProcessedEmailEntity).
              | Expected:
              |""".trimMargin() + _infoProcessedEmails + """
              |
              | Found:
              |""".trimMargin() + _existingProcessedEmails)
        }
        val _columnsDrafts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDrafts.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("emailId", TableInfo.Column("emailId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("threadId", TableInfo.Column("threadId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("subject", TableInfo.Column("subject", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("recipient", TableInfo.Column("recipient", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("body", TableInfo.Column("body", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("pushedToGmail", TableInfo.Column("pushedToGmail", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrafts.put("gmailDraftId", TableInfo.Column("gmailDraftId", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDrafts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDrafts: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDrafts: TableInfo = TableInfo("drafts", _columnsDrafts, _foreignKeysDrafts,
            _indicesDrafts)
        val _existingDrafts: TableInfo = read(connection, "drafts")
        if (!_infoDrafts.equals(_existingDrafts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |drafts(com.example.ai_agent.data.local.DraftEntity).
              | Expected:
              |""".trimMargin() + _infoDrafts + """
              |
              | Found:
              |""".trimMargin() + _existingDrafts)
        }
        val _columnsMeetingReminders: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMeetingReminders.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMeetingReminders.put("eventId", TableInfo.Column("eventId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMeetingReminders.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMeetingReminders.put("startTime", TableInfo.Column("startTime", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMeetingReminders.put("reminderType", TableInfo.Column("reminderType", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMeetingReminders.put("scheduledAt", TableInfo.Column("scheduledAt", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMeetingReminders.put("fired", TableInfo.Column("fired", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsMeetingReminders.put("source", TableInfo.Column("source", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMeetingReminders: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMeetingReminders: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMeetingReminders: TableInfo = TableInfo("meeting_reminders",
            _columnsMeetingReminders, _foreignKeysMeetingReminders, _indicesMeetingReminders)
        val _existingMeetingReminders: TableInfo = read(connection, "meeting_reminders")
        if (!_infoMeetingReminders.equals(_existingMeetingReminders)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |meeting_reminders(com.example.ai_agent.data.local.MeetingReminderEntity).
              | Expected:
              |""".trimMargin() + _infoMeetingReminders + """
              |
              | Found:
              |""".trimMargin() + _existingMeetingReminders)
        }
        val _columnsSyncState: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSyncState.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncState.put("lastSyncAt", TableInfo.Column("lastSyncAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncState.put("lastHistoryId", TableInfo.Column("lastHistoryId", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncState.put("lastError", TableInfo.Column("lastError", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncState.put("emailsProcessed", TableInfo.Column("emailsProcessed", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSyncState: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSyncState: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSyncState: TableInfo = TableInfo("sync_state", _columnsSyncState,
            _foreignKeysSyncState, _indicesSyncState)
        val _existingSyncState: TableInfo = read(connection, "sync_state")
        if (!_infoSyncState.equals(_existingSyncState)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sync_state(com.example.ai_agent.data.local.SyncStateEntity).
              | Expected:
              |""".trimMargin() + _infoSyncState + """
              |
              | Found:
              |""".trimMargin() + _existingSyncState)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "processed_emails", "drafts",
        "meeting_reminders", "sync_state")
  }

  public override fun clearAllTables() {
    super.performClear(false, "processed_emails", "drafts", "meeting_reminders", "sync_state")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ProcessedEmailDao::class, ProcessedEmailDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DraftDao::class, DraftDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MeetingReminderDao::class,
        MeetingReminderDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SyncStateDao::class, SyncStateDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun processedEmailDao(): ProcessedEmailDao = _processedEmailDao.value

  public override fun draftDao(): DraftDao = _draftDao.value

  public override fun meetingReminderDao(): MeetingReminderDao = _meetingReminderDao.value

  public override fun syncStateDao(): SyncStateDao = _syncStateDao.value
}
