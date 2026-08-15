package com.example.ai_agent.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
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
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ProcessedEmailDao_Impl(
  __db: RoomDatabase,
) : ProcessedEmailDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfProcessedEmailEntity: EntityInsertAdapter<ProcessedEmailEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfProcessedEmailEntity = object :
        EntityInsertAdapter<ProcessedEmailEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `processed_emails` (`emailId`,`threadId`,`subject`,`sender`,`priority`,`summary`,`actionItemsJson`,`receivedAt`,`processedAt`,`requiresReply`) VALUES (?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ProcessedEmailEntity) {
        statement.bindText(1, entity.emailId)
        statement.bindText(2, entity.threadId)
        statement.bindText(3, entity.subject)
        statement.bindText(4, entity.sender)
        statement.bindText(5, entity.priority)
        statement.bindText(6, entity.summary)
        statement.bindText(7, entity.actionItemsJson)
        statement.bindLong(8, entity.receivedAt)
        statement.bindLong(9, entity.processedAt)
        val _tmp: Int = if (entity.requiresReply) 1 else 0
        statement.bindLong(10, _tmp.toLong())
      }
    }
  }

  public override suspend fun insert(entity: ProcessedEmailEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfProcessedEmailEntity.insert(_connection, entity)
  }

  public override fun observeAll(): Flow<List<ProcessedEmailEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM processed_emails 
        |        ORDER BY 
        |            CASE priority 
        |                WHEN 'CRITICAL' THEN 0 
        |                WHEN 'HIGH' THEN 1 
        |                WHEN 'MEDIUM' THEN 2 
        |                WHEN 'LOW' THEN 3 
        |                ELSE 4 
        |            END ASC, 
        |            receivedAt DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("processed_emails")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfEmailId: Int = getColumnIndexOrThrow(_stmt, "emailId")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "threadId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfSender: Int = getColumnIndexOrThrow(_stmt, "sender")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfActionItemsJson: Int = getColumnIndexOrThrow(_stmt, "actionItemsJson")
        val _columnIndexOfReceivedAt: Int = getColumnIndexOrThrow(_stmt, "receivedAt")
        val _columnIndexOfProcessedAt: Int = getColumnIndexOrThrow(_stmt, "processedAt")
        val _columnIndexOfRequiresReply: Int = getColumnIndexOrThrow(_stmt, "requiresReply")
        val _result: MutableList<ProcessedEmailEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProcessedEmailEntity
          val _tmpEmailId: String
          _tmpEmailId = _stmt.getText(_columnIndexOfEmailId)
          val _tmpThreadId: String
          _tmpThreadId = _stmt.getText(_columnIndexOfThreadId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpSender: String
          _tmpSender = _stmt.getText(_columnIndexOfSender)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpSummary: String
          _tmpSummary = _stmt.getText(_columnIndexOfSummary)
          val _tmpActionItemsJson: String
          _tmpActionItemsJson = _stmt.getText(_columnIndexOfActionItemsJson)
          val _tmpReceivedAt: Long
          _tmpReceivedAt = _stmt.getLong(_columnIndexOfReceivedAt)
          val _tmpProcessedAt: Long
          _tmpProcessedAt = _stmt.getLong(_columnIndexOfProcessedAt)
          val _tmpRequiresReply: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfRequiresReply).toInt()
          _tmpRequiresReply = _tmp != 0
          _item =
              ProcessedEmailEntity(_tmpEmailId,_tmpThreadId,_tmpSubject,_tmpSender,_tmpPriority,_tmpSummary,_tmpActionItemsJson,_tmpReceivedAt,_tmpProcessedAt,_tmpRequiresReply)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTopPrioritized(): Flow<List<ProcessedEmailEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM processed_emails 
        |        ORDER BY 
        |            CASE priority 
        |                WHEN 'CRITICAL' THEN 0 
        |                WHEN 'HIGH' THEN 1 
        |                WHEN 'MEDIUM' THEN 2 
        |                WHEN 'LOW' THEN 3 
        |                ELSE 4 
        |            END ASC, 
        |            receivedAt DESC 
        |        LIMIT 10
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("processed_emails")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfEmailId: Int = getColumnIndexOrThrow(_stmt, "emailId")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "threadId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfSender: Int = getColumnIndexOrThrow(_stmt, "sender")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfActionItemsJson: Int = getColumnIndexOrThrow(_stmt, "actionItemsJson")
        val _columnIndexOfReceivedAt: Int = getColumnIndexOrThrow(_stmt, "receivedAt")
        val _columnIndexOfProcessedAt: Int = getColumnIndexOrThrow(_stmt, "processedAt")
        val _columnIndexOfRequiresReply: Int = getColumnIndexOrThrow(_stmt, "requiresReply")
        val _result: MutableList<ProcessedEmailEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProcessedEmailEntity
          val _tmpEmailId: String
          _tmpEmailId = _stmt.getText(_columnIndexOfEmailId)
          val _tmpThreadId: String
          _tmpThreadId = _stmt.getText(_columnIndexOfThreadId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpSender: String
          _tmpSender = _stmt.getText(_columnIndexOfSender)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpSummary: String
          _tmpSummary = _stmt.getText(_columnIndexOfSummary)
          val _tmpActionItemsJson: String
          _tmpActionItemsJson = _stmt.getText(_columnIndexOfActionItemsJson)
          val _tmpReceivedAt: Long
          _tmpReceivedAt = _stmt.getLong(_columnIndexOfReceivedAt)
          val _tmpProcessedAt: Long
          _tmpProcessedAt = _stmt.getLong(_columnIndexOfProcessedAt)
          val _tmpRequiresReply: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfRequiresReply).toInt()
          _tmpRequiresReply = _tmp != 0
          _item =
              ProcessedEmailEntity(_tmpEmailId,_tmpThreadId,_tmpSubject,_tmpSender,_tmpPriority,_tmpSummary,_tmpActionItemsJson,_tmpReceivedAt,_tmpProcessedAt,_tmpRequiresReply)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllIds(): List<String> {
    val _sql: String = "SELECT emailId FROM processed_emails"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(emailId: String): ProcessedEmailEntity? {
    val _sql: String = "SELECT * FROM processed_emails WHERE emailId = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, emailId)
        val _columnIndexOfEmailId: Int = getColumnIndexOrThrow(_stmt, "emailId")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "threadId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfSender: Int = getColumnIndexOrThrow(_stmt, "sender")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _columnIndexOfActionItemsJson: Int = getColumnIndexOrThrow(_stmt, "actionItemsJson")
        val _columnIndexOfReceivedAt: Int = getColumnIndexOrThrow(_stmt, "receivedAt")
        val _columnIndexOfProcessedAt: Int = getColumnIndexOrThrow(_stmt, "processedAt")
        val _columnIndexOfRequiresReply: Int = getColumnIndexOrThrow(_stmt, "requiresReply")
        val _result: ProcessedEmailEntity?
        if (_stmt.step()) {
          val _tmpEmailId: String
          _tmpEmailId = _stmt.getText(_columnIndexOfEmailId)
          val _tmpThreadId: String
          _tmpThreadId = _stmt.getText(_columnIndexOfThreadId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpSender: String
          _tmpSender = _stmt.getText(_columnIndexOfSender)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpSummary: String
          _tmpSummary = _stmt.getText(_columnIndexOfSummary)
          val _tmpActionItemsJson: String
          _tmpActionItemsJson = _stmt.getText(_columnIndexOfActionItemsJson)
          val _tmpReceivedAt: Long
          _tmpReceivedAt = _stmt.getLong(_columnIndexOfReceivedAt)
          val _tmpProcessedAt: Long
          _tmpProcessedAt = _stmt.getLong(_columnIndexOfProcessedAt)
          val _tmpRequiresReply: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfRequiresReply).toInt()
          _tmpRequiresReply = _tmp != 0
          _result =
              ProcessedEmailEntity(_tmpEmailId,_tmpThreadId,_tmpSubject,_tmpSender,_tmpPriority,_tmpSummary,_tmpActionItemsJson,_tmpReceivedAt,_tmpProcessedAt,_tmpRequiresReply)
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
