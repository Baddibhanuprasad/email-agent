package com.example.ai_agent.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SyncStateDao_Impl(
  __db: RoomDatabase,
) : SyncStateDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSyncStateEntity: EntityInsertAdapter<SyncStateEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSyncStateEntity = object : EntityInsertAdapter<SyncStateEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `sync_state` (`id`,`lastSyncAt`,`lastHistoryId`,`lastError`,`emailsProcessed`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SyncStateEntity) {
        statement.bindLong(1, entity.id.toLong())
        val _tmpLastSyncAt: Long? = entity.lastSyncAt
        if (_tmpLastSyncAt == null) {
          statement.bindNull(2)
        } else {
          statement.bindLong(2, _tmpLastSyncAt)
        }
        val _tmpLastHistoryId: String? = entity.lastHistoryId
        if (_tmpLastHistoryId == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpLastHistoryId)
        }
        val _tmpLastError: String? = entity.lastError
        if (_tmpLastError == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpLastError)
        }
        statement.bindLong(5, entity.emailsProcessed.toLong())
      }
    }
  }

  public override suspend fun upsert(entity: SyncStateEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfSyncStateEntity.insert(_connection, entity)
  }

  public override fun observe(): Flow<SyncStateEntity?> {
    val _sql: String = "SELECT * FROM sync_state WHERE id = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("sync_state")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLastSyncAt: Int = getColumnIndexOrThrow(_stmt, "lastSyncAt")
        val _columnIndexOfLastHistoryId: Int = getColumnIndexOrThrow(_stmt, "lastHistoryId")
        val _columnIndexOfLastError: Int = getColumnIndexOrThrow(_stmt, "lastError")
        val _columnIndexOfEmailsProcessed: Int = getColumnIndexOrThrow(_stmt, "emailsProcessed")
        val _result: SyncStateEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpLastSyncAt: Long?
          if (_stmt.isNull(_columnIndexOfLastSyncAt)) {
            _tmpLastSyncAt = null
          } else {
            _tmpLastSyncAt = _stmt.getLong(_columnIndexOfLastSyncAt)
          }
          val _tmpLastHistoryId: String?
          if (_stmt.isNull(_columnIndexOfLastHistoryId)) {
            _tmpLastHistoryId = null
          } else {
            _tmpLastHistoryId = _stmt.getText(_columnIndexOfLastHistoryId)
          }
          val _tmpLastError: String?
          if (_stmt.isNull(_columnIndexOfLastError)) {
            _tmpLastError = null
          } else {
            _tmpLastError = _stmt.getText(_columnIndexOfLastError)
          }
          val _tmpEmailsProcessed: Int
          _tmpEmailsProcessed = _stmt.getLong(_columnIndexOfEmailsProcessed).toInt()
          _result =
              SyncStateEntity(_tmpId,_tmpLastSyncAt,_tmpLastHistoryId,_tmpLastError,_tmpEmailsProcessed)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun `get`(): SyncStateEntity? {
    val _sql: String = "SELECT * FROM sync_state WHERE id = 1 LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLastSyncAt: Int = getColumnIndexOrThrow(_stmt, "lastSyncAt")
        val _columnIndexOfLastHistoryId: Int = getColumnIndexOrThrow(_stmt, "lastHistoryId")
        val _columnIndexOfLastError: Int = getColumnIndexOrThrow(_stmt, "lastError")
        val _columnIndexOfEmailsProcessed: Int = getColumnIndexOrThrow(_stmt, "emailsProcessed")
        val _result: SyncStateEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpLastSyncAt: Long?
          if (_stmt.isNull(_columnIndexOfLastSyncAt)) {
            _tmpLastSyncAt = null
          } else {
            _tmpLastSyncAt = _stmt.getLong(_columnIndexOfLastSyncAt)
          }
          val _tmpLastHistoryId: String?
          if (_stmt.isNull(_columnIndexOfLastHistoryId)) {
            _tmpLastHistoryId = null
          } else {
            _tmpLastHistoryId = _stmt.getText(_columnIndexOfLastHistoryId)
          }
          val _tmpLastError: String?
          if (_stmt.isNull(_columnIndexOfLastError)) {
            _tmpLastError = null
          } else {
            _tmpLastError = _stmt.getText(_columnIndexOfLastError)
          }
          val _tmpEmailsProcessed: Int
          _tmpEmailsProcessed = _stmt.getLong(_columnIndexOfEmailsProcessed).toInt()
          _result =
              SyncStateEntity(_tmpId,_tmpLastSyncAt,_tmpLastHistoryId,_tmpLastError,_tmpEmailsProcessed)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
