package com.example.ai_agent.data.google

import android.content.Context
import com.example.ai_agent.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.tasks.Tasks
import com.google.api.services.tasks.TasksScopes

class GoogleAuthManager(private val context: Context) {

    private val scopes = listOf(
        GmailScopes.GMAIL_READONLY,
        GmailScopes.GMAIL_COMPOSE,
        CalendarScopes.CALENDAR,
        TasksScopes.TASKS
    )

    val signInClient: GoogleSignInClient by lazy {
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope(GmailScopes.GMAIL_READONLY),
                Scope(GmailScopes.GMAIL_COMPOSE),
                Scope(CalendarScopes.CALENDAR),
                Scope(TasksScopes.TASKS)
            )
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isNotBlank()) {
            builder.requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
        }
        GoogleSignIn.getClient(context, builder.build())
    }

    fun getSignedInAccount(): GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)

    fun getUserEmail(): String? = getSignedInAccount()?.email

    fun isSignedIn(): Boolean {
        val account = getSignedInAccount() ?: return false
        return GoogleSignIn.hasPermissions(account, *scopes.map { Scope(it) }.toTypedArray())
    }

    fun credential(): GoogleAccountCredential? {
        val account = getSignedInAccount()?.account ?: return null
        return GoogleAccountCredential.usingOAuth2(context, scopes).setSelectedAccount(account)
    }

    fun gmail(): Gmail? {
        val cred = credential() ?: return null
        return Gmail.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), cred)
            .setApplicationName("Ai Agent")
            .build()
    }

    fun calendar(): Calendar? {
        val cred = credential() ?: return null
        return Calendar.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), cred)
            .setApplicationName("Ai Agent")
            .build()
    }

    fun tasks(): Tasks? {
        val cred = credential() ?: return null
        return Tasks.Builder(NetHttpTransport(), GsonFactory.getDefaultInstance(), cred)
            .setApplicationName("Ai Agent")
            .build()
    }
}
