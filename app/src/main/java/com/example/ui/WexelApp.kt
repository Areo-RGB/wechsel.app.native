package com.example.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.TabId
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.AufstellungScreen
import com.example.ui.screens.DatenScreen
import com.example.ui.screens.KaderScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WexelApp(
    viewModel: WexelViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val players by viewModel.players.collectAsStateWithLifecycle()
    val matchState by viewModel.matchState.collectAsStateWithLifecycle()
    val timeDisplayMode by viewModel.timeDisplayMode.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val isFullscreen by viewModel.isFullscreen.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Listen to messages for snackbar notifications
    LaunchedEffect(viewModel) {
        viewModel.messages.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle Immersive Fullscreen Mode
    LaunchedEffect(isFullscreen) {
        val activity = context as? Activity
        if (activity != null) {
            val window = activity.window
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            if (isFullscreen) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomNavBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.setActiveTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                TabId.KADER -> {
                    KaderScreen(
                        players = players,
                        isFullscreen = isFullscreen,
                        onToggleFullscreen = { viewModel.toggleFullscreen() },
                        onCycleStatus = { viewModel.cyclePlayerStatus(it) },
                        onAddPlayer = { viewModel.addPlayer(it) },
                        onDeletePlayer = { viewModel.deletePlayer(it) },
                        onNavigateToAufstellung = { viewModel.setActiveTab(TabId.AUFSTELLUNG) }
                    )
                }
                TabId.AUFSTELLUNG -> {
                    AufstellungScreen(
                        players = players,
                        matchState = matchState,
                        timeDisplayMode = timeDisplayMode,
                        isFullscreen = isFullscreen,
                        onToggleMatch = { viewModel.toggleMatch() },
                        onResetMatch = { viewModel.resetMatch() },
                        onToggleFullscreen = { viewModel.toggleFullscreen() },
                        onCycleTimeMode = { viewModel.cycleTimeDisplayMode() },
                        onBringToField = { viewModel.bringToField(it) },
                        onMoveToBench = { viewModel.moveToBench(it) }
                    )
                }
                TabId.DATEN -> {
                    DatenScreen(
                        players = players,
                        matchElapsed = matchState.elapsed,
                        isFullscreen = isFullscreen,
                        onToggleFullscreen = { viewModel.toggleFullscreen() }
                    )
                }
            }
        }
    }
}
