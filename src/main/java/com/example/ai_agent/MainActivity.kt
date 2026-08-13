package com.example.ai_agent

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai_agent.ui.AgentViewModel
import com.example.ai_agent.ui.navigation.NavRoutes
import com.example.ai_agent.ui.screens.DashboardScreen
import com.example.ai_agent.ui.screens.DraftsScreen
import com.example.ai_agent.ui.screens.LoginScreen
import com.example.ai_agent.ui.screens.SettingsScreen
import com.example.ai_agent.ui.theme.AiagentTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val app get() = application as AiAgentApplication

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        runCatching {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            task.getResult(ApiException::class.java)
            signedInState.value = true
            com.example.ai_agent.worker.SyncScheduler.enqueueSync(app)
        }.onFailure {
            Toast.makeText(this, "Sign-in failed: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    private var signedInState = mutableStateOf(false)

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        signedInState.value = app.googleAuthManager.isSignedIn()
        enableEdgeToEdge()
        setContent {
            AiagentTheme {
                val viewModel: AgentViewModel = viewModel()
                var signedIn by signedInState
                val scope = rememberCoroutineScope()

                if (!signedIn) {
                    LoginScreen(
                        onSignInClick = {
                            signInLauncher.launch(app.googleAuthManager.signInClient.signInIntent)
                        }
                    )
                } else {
                    MainShell(
                        viewModel = viewModel,
                        onSignOut = {
                            scope.launch {
                                viewModel.signOut()
                                signedIn = false
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MainShell(viewModel: AgentViewModel, onSignOut: () -> Unit) {
    var currentRoute by rememberSaveable { mutableStateOf(NavRoutes.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == NavRoutes.HOME,
                    onClick = { currentRoute = NavRoutes.HOME },
                    icon = { Icon(Icons.Default.Email, contentDescription = null) },
                    label = { Text("Inbox") }
                )
                NavigationBarItem(
                    selected = currentRoute == NavRoutes.DRAFTS,
                    onClick = { currentRoute = NavRoutes.DRAFTS },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    label = { Text("Drafts") }
                )
                NavigationBarItem(
                    selected = currentRoute == NavRoutes.SETTINGS,
                    onClick = { currentRoute = NavRoutes.SETTINGS },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") }
                )
            }
        }
    ) { padding ->
        when (currentRoute) {
            NavRoutes.HOME -> DashboardScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(padding)
            )
            NavRoutes.DRAFTS -> DraftsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(padding)
            )
            NavRoutes.SETTINGS -> SettingsScreen(
                viewModel = viewModel,
                onSignOut = onSignOut,
                modifier = Modifier.padding(padding)
            )
        }
    }
}
