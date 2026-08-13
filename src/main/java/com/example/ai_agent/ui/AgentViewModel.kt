package com.example.ai_agent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_agent.AiAgentApplication
import com.example.ai_agent.data.local.ProcessedEmailEntity
import com.example.ai_agent.data.local.SyncStateEntity
import com.example.ai_agent.data.model.DraftPreview
import com.example.ai_agent.data.model.EmailPriority
import com.example.ai_agent.worker.SyncScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class AgentViewModel : ViewModel() {

    private val app = AiAgentApplication.instance
    private val repository = app.agentRepository
    private val authManager = app.googleAuthManager
    private val json = Json { ignoreUnknownKeys = true }

    val isSignedIn: Boolean get() = authManager.isSignedIn()
    val userEmail: String? get() = authManager.getUserEmail()

    val emails: StateFlow<List<ProcessedEmailUi>> = repository.processedEmails
        .map { list -> list.map { it.toUi() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val drafts: StateFlow<List<DraftPreview>> = repository.drafts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val syncState: StateFlow<SyncStateEntity?> = repository.syncState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val geminiKeys: StateFlow<List<String>> = repository.processedEmails
        .map { repository.getGeminiKeys() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getGeminiKeys())

    private val _isSyncing = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    val hasGeminiKey: Boolean get() = repository.getGeminiKeys().isNotEmpty()

    fun refreshKeys() {
        viewModelScope.launch {
            // trigger recomposition via sync state read
        }
    }

    fun getGeminiKeysList(): List<String> = repository.getGeminiKeys()

    fun addGeminiKey(key: String) {
        repository.addGeminiKey(key)
        syncNow()
    }

    fun removeGeminiKey(key: String) {
        repository.removeGeminiKey(key)
    }

    fun syncNow() {
        viewModelScope.launch {
            _isSyncing.value = true
            repository.runSync()
            _isSyncing.value = false
        }
        SyncScheduler.enqueueSync(app)
    }

    fun updateDraft(id: Long, body: String) {
        viewModelScope.launch { repository.updateDraft(id, body) }
    }

    fun pushDraftToGmail(id: Long, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            onResult(repository.pushDraftToGmail(id))
        }
    }

    suspend fun signOut() {
        kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            authManager.signInClient.signOut().addOnCompleteListener {
                cont.resume(Unit) {}
            }
        }
    }

    private fun ProcessedEmailEntity.toUi(): ProcessedEmailUi {
        val items = runCatching {
            json.decodeFromString(ListSerializer(String.serializer()), actionItemsJson)
        }.getOrDefault(emptyList())
        return ProcessedEmailUi(
            emailId = emailId,
            subject = subject,
            sender = sender,
            priority = runCatching { EmailPriority.valueOf(priority) }.getOrDefault(EmailPriority.MEDIUM),
            summary = summary,
            actionItems = items,
            receivedAt = receivedAt,
            requiresReply = requiresReply
        )
    }
}

data class ProcessedEmailUi(
    val emailId: String,
    val subject: String,
    val sender: String,
    val priority: EmailPriority,
    val summary: String,
    val actionItems: List<String>,
    val receivedAt: Long,
    val requiresReply: Boolean
)
