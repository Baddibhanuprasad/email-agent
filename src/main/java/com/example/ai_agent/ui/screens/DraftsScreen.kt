package com.example.ai_agent.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ai_agent.data.model.DraftPreview
import com.example.ai_agent.ui.AgentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftsScreen(viewModel: AgentViewModel, modifier: Modifier = Modifier) {
    val drafts by viewModel.drafts.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Draft Replies") }) }
    ) { padding ->
        if (drafts.isEmpty()) {
            Text(
                text = "No drafts yet. AI-generated replies appear here for your approval.",
                modifier = Modifier.padding(padding).padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(drafts, key = { it.id }) { draft ->
                    DraftCard(draft, viewModel)
                }
            }
        }
    }
}

@Composable
private fun DraftCard(draft: DraftPreview, viewModel: AgentViewModel) {
    var body by remember(draft.id) { mutableStateOf(draft.body) }
    var status by remember(draft.id) { mutableStateOf<String?>(null) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = draft.subject)
            Text(text = "To: ${draft.recipient}", modifier = Modifier.padding(top = 4.dp))
            OutlinedTextField(
                value = body,
                onValueChange = {
                    body = it
                    viewModel.updateDraft(draft.id, it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                minLines = 5
            )
            Button(
                onClick = {
                    viewModel.pushDraftToGmail(draft.id) { result ->
                        status = result.fold(
                            onSuccess = { "Saved to Gmail drafts (not sent)" },
                            onFailure = { it.message ?: "Failed" }
                        )
                    }
                },
                enabled = !draft.pushedToGmail,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(if (draft.pushedToGmail) "Already in Gmail" else "Create Gmail Draft")
            }
            status?.let { Text(text = it, modifier = Modifier.padding(top = 8.dp)) }
        }
    }
}
