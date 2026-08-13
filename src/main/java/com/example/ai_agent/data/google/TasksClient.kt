package com.example.ai_agent.data.google

import com.google.api.services.tasks.Tasks

class TasksClient(private val tasks: Tasks) {

    private var defaultListId: String? = null

    private fun getDefaultListId(): String {
        defaultListId?.let { return it }
        val list = tasks.tasklists().list().setMaxResults(1).execute()
            .items?.firstOrNull()
            ?: tasks.tasklists().insert(
                com.google.api.services.tasks.model.TaskList().setTitle("AI Agent Tasks")
            ).execute()
        defaultListId = list.id
        return list.id
    }

    fun addTask(title: String, notes: String?) {
        val task = com.google.api.services.tasks.model.Task()
            .setTitle(title)
            .setNotes(notes)
        tasks.tasks().insert(getDefaultListId(), task).execute()
    }

    fun addTasks(items: List<String>, emailSubject: String) {
        items.forEach { item ->
            addTask(item, "From email: $emailSubject")
        }
    }
}
