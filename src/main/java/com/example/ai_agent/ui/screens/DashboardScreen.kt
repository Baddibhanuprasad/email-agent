package com.example.ai_agent.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ai_agent.data.model.EmailPriority
import com.example.ai_agent.ui.AgentViewModel
import com.example.ai_agent.ui.ProcessedEmailUi
import java.text.DateFormat
import java.util.Date

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: AgentViewModel, modifier: Modifier = Modifier) {
    val emails by viewModel.emails.collectAsState()
    val syncState by viewModel.syncState.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val hasKey = viewModel.hasGeminiKey
    val userEmail = viewModel.userEmail.orEmpty()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Prioritized Inbox") },
                actions = {
                    if (isSyncing) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        androidx.compose.material3.IconButton(onClick = { viewModel.syncNow() }) {
                            androidx.compose.material3.Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Sync Now"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (!hasKey) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "⚠️ Gemini API Key Required",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "Please go to Settings tab and add your Gemini API key to enable AI email reading, summarization, prioritization, task creation, and calendar scheduling.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            userEmail.takeIf { it.isNotBlank() }?.let { email ->
                Text(
                    text = "Account: $email",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            syncState?.let { state ->
                Text(
                    text = buildString {
                        append("Last sync: ")
                        append(
                            state.lastSyncAt?.let { DateFormat.getDateTimeInstance().format(Date(it)) }
                                ?: "Never"
                        )
                        state.lastError?.let { append(" • Error: $it") }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            if (emails.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSyncing) "Syncing emails with Gemini AI…" else "No processed emails yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    if (!isSyncing && hasKey) {
                        androidx.compose.material3.Button(onClick = { viewModel.syncNow() }) {
                            Text("Sync Emails Now")
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(emails, key = { it.emailId }) { email ->
                        EmailCard(email)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmailCard(email: ProcessedEmailUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = priorityColor(email.priority).copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = email.subject, style = MaterialTheme.typography.titleMedium)
            Text(
                text = email.sender,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = email.priority.label,
                color = priorityColor(email.priority),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = email.summary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            if (email.actionItems.isNotEmpty()) {
                Text(
                    text = "Tasks: ${email.actionItems.joinToString(" • ")}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

private fun priorityColor(priority: EmailPriority): Color = when (priority) {
    EmailPriority.CRITICAL -> Color(0xFFB00020)
    EmailPriority.HIGH -> Color(0xFFE65100)
    EmailPriority.MEDIUM -> Color(0xFF1565C0)
    EmailPriority.LOW -> Color(0xFF558B2F)
}
