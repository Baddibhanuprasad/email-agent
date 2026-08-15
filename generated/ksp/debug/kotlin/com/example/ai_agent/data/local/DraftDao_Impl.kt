package com.example.ai_agent.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
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
public class DraftDao_Impl(
  __db: RoomDatabase,
) : DraftDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDraftEntity: EntityInsertAdapter<DraftEntity>

  private val __updateAdapterOfDraftEntity: EntityDeleteOrUpdateAdapter<DraftEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDraftEntity = object : EntityInsertAdapter<DraftEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `drafts` (`id`,`emailId`,`threadId`,`subject`,`recipient`,`body`,`createdAt`,`pushedToGmail`,`gmailDraftId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DraftEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.emailId)
        statement.bindText(3, entity.threadId)
        statement.bindText(4, entity.subject)
        statement.bindText(5, entity.recipient)
        statement.bindText(6, entity.body)
        statement.bindLong(7, entity.createdAt)
        val _tmp: Int = if (entity.pushedToGmail) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmpGmailDraftId: String? = entity.gmailDraftId
        if (_tmpGmailDraftId == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpGmailDraftId)
        }
      }
    }
    this.__updateAdapterOfDraftEntity = object : EntityDeleteOrUpdateAdapter<DraftEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `drafts` SET `id` = ?,`emailId` = ?,`threadId` = ?,`subject` = ?,`recipient` = ?,`body` = ?,`createdAt` = ?,`pushedToGmail` = ?,`gmailDraftId` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DraftEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.emailId)
        statement.bindText(3, entity.threadId)
        statement.bindText(4, entity.subject)
        statement.bindText(5, entity.recipient)
        statement.bindText(6, entity.body)
        statement.bindLong(7, entity.createdAt)
        val _tmp: Int = if (entity.pushedToGmail) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmpGmailDraftId: String? = entity.gmailDraftId
        if (_tmpGmailDraftId == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpGmailDraftId)
        }
        statement.bindLong(10, entity.id)
      }
    }
  }

  public override suspend fun insert(entity: DraftEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfDraftEntity.insertAndReturnId(_connection, entity)
    _result
  }

  public override suspend fun update(entity: DraftEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfDraftEntity.handle(_connection, entity)
  }

  public override fun observeAll(): Flow<List<DraftEntity>> {
    val _sql: String = "SELECT * FROM drafts ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("drafts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfEmailId: Int = getColumnIndexOrThrow(_stmt, "emailId")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "threadId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfRecipient: Int = getColumnIndexOrThrow(_stmt, "recipient")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfPushedToGmail: Int = getColumnIndexOrThrow(_stmt, "pushedToGmail")
        val _columnIndexOfGmailDraftId: Int = getColumnIndexOrThrow(_stmt, "gmailDraftId")
        val _result: MutableList<DraftEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DraftEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpEmailId: String
          _tmpEmailId = _stmt.getText(_columnIndexOfEmailId)
          val _tmpThreadId: String
          _tmpThreadId = _stmt.getText(_columnIndexOfThreadId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpRecipient: String
          _tmpRecipient = _stmt.getText(_columnIndexOfRecipient)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpPushedToGmail: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPushedToGmail).toInt()
          _tmpPushedToGmail = _tmp != 0
          val _tmpGmailDraftId: String?
          if (_stmt.isNull(_columnIndexOfGmailDraftId)) {
            _tmpGmailDraftId = null
          } else {
            _tmpGmailDraftId = _stmt.getText(_columnIndexOfGmailDraftId)
          }
          _item =
              DraftEntity(_tmpId,_tmpEmailId,_tmpThreadId,_tmpSubject,_tmpRecipient,_tmpBody,_tmpCreatedAt,_tmpPushedToGmail,_tmpGmailDraftId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): DraftEntity? {
    val _sql: String = "SELECT * FROM drafts WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfEmailId: Int = getColumnIndexOrThrow(_stmt, "emailId")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "threadId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfRecipient: Int = getColumnIndexOrThrow(_stmt, "recipient")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfPushedToGmail: Int = getColumnIndexOrThrow(_stmt, "pushedToGmail")
        val _columnIndexOfGmailDraftId: Int = getColumnIndexOrThrow(_stmt, "gmailDraftId")
        val _result: DraftEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpEmailId: String
          _tmpEmailId = _stmt.getText(_columnIndexOfEmailId)
          val _tmpThreadId: String
          _tmpThreadId = _stmt.getText(_columnIndexOfThreadId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpRecipient: String
          _tmpRecipient = _stmt.getText(_columnIndexOfRecipient)
          val _tmpBody: String
          _tmpBody = _stmt.getText(_columnIndexOfBody)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpPushedToGmail: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPushedToGmail).toInt()
          _tmpPushedToGmail = _tmp != 0
          val _tmpGmailDraftId: String?
          if (_stmt.isNull(_columnIndexOfGmailDraftId)) {
            _tmpGmailDraftId = null
          } else {
            _tmpGmailDraftId = _stmt.getText(_columnIndexOfGmailDraftId)
          }
          _result =
              DraftEntity(_tmpId,_tmpEmailId,_tmpThreadId,_tmpSubject,_tmpRecipient,_tmpBody,_tmpCreatedAt,_tmpPushedToGmail,_tmpGmailDraftId)
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
