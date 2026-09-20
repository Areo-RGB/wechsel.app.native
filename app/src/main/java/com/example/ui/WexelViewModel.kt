package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.MatchState
import com.example.model.MatchStatus
import com.example.model.Player
import com.example.model.PlayerStatus
import com.example.model.TabId
import com.example.model.TimeDisplayMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WexelViewModel : ViewModel() {

    companion object {
        const val MAX_FIELD_COUNT = 8

        private val DEFAULT_PLAYERS = listOf(
            // Starting 8 on field
            Player("p1", "Silas", PlayerStatus.FIELD),
            Player("p2", "Finley", PlayerStatus.FIELD),
            Player("p3", "Arvid", PlayerStatus.FIELD),
            Player("p4", "Lion", PlayerStatus.FIELD),
            Player("p5", "Jakob", PlayerStatus.FIELD),
            Player("p6", "Paul", PlayerStatus.FIELD),
            Player("p7", "Lennox", PlayerStatus.FIELD),
            Player("p8", "Levi", PlayerStatus.FIELD),
            // Bench
            Player("p9", "Lasse", PlayerStatus.BENCH),
            Player("p10", "Milan", PlayerStatus.BENCH),
            Player("p11", "Lionel", PlayerStatus.BENCH),
            Player("p12", "Arturo", PlayerStatus.BENCH),
            Player("p13", "Peter", PlayerStatus.BENCH),
            Player("p14", "Tommy", PlayerStatus.BENCH),
            Player("p15", "Alex", PlayerStatus.BENCH),
            Player("p16", "Tayo", PlayerStatus.BENCH)
        )
    }

    private val _players = MutableStateFlow(DEFAULT_PLAYERS)
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _matchState = MutableStateFlow(MatchState())
    val matchState: StateFlow<MatchState> = _matchState.asStateFlow()

    private val _timeDisplayMode = MutableStateFlow(TimeDisplayMode.BOTH)
    val timeDisplayMode: StateFlow<TimeDisplayMode> = _timeDisplayMode.asStateFlow()

    private val _activeTab = MutableStateFlow(TabId.KADER)
    val activeTab: StateFlow<TabId> = _activeTab.asStateFlow()

    private val _isFullscreen = MutableStateFlow(true)
    val isFullscreen: StateFlow<Boolean> = _isFullscreen.asStateFlow()

    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 10)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    init {
        // Main ticker coroutine
        viewModelScope.launch {
            while (isActive) {
                delay(1000)
                if (_matchState.value.status == MatchStatus.RUNNING) {
                    tick()
                }
            }
        }
    }

    private fun tick() {
        _matchState.update { it.copy(elapsed = it.elapsed + 1) }
        _players.update { list ->
            list.map { player ->
                when (player.status) {
                    PlayerStatus.FIELD -> player.copy(feldzeit = player.feldzeit + 1)
                    PlayerStatus.BENCH -> player.copy(bankzeit = player.bankzeit + 1)
                }
            }
        }
    }

    fun toggleMatch() {
        _matchState.update { current ->
            val nextStatus = if (current.status == MatchStatus.RUNNING) {
                MatchStatus.PAUSED
            } else {
                MatchStatus.RUNNING
            }
            current.copy(status = nextStatus)
        }
    }

    fun resetMatch() {
        _matchState.value = MatchState(status = MatchStatus.IDLE, elapsed = 0)
        _players.update { list ->
            list.map { it.copy(feldzeit = 0L, bankzeit = 0L) }
        }
        _messages.tryEmit("Spielzeit & Statistiken zurückgesetzt")
    }

    fun cyclePlayerStatus(playerId: String) {
        _players.update { list ->
            val player = list.find { it.id == playerId } ?: return@update list
            val currentFieldCount = list.count { it.status == PlayerStatus.FIELD }

            if (player.status == PlayerStatus.BENCH && currentFieldCount >= MAX_FIELD_COUNT) {
                _messages.tryEmit("Feld ist voll ($MAX_FIELD_COUNT/$MAX_FIELD_COUNT) – zuerst Spieler auf die Bank")
                return@update list
            }

            val nextStatus = if (player.status == PlayerStatus.FIELD) {
                PlayerStatus.BENCH
            } else {
                PlayerStatus.FIELD
            }

            list.map {
                if (it.id == playerId) it.copy(status = nextStatus) else it
            }
        }
    }

    fun bringToField(playerId: String) {
        val currentFieldCount = _players.value.count { it.status == PlayerStatus.FIELD }
        val player = _players.value.find { it.id == playerId } ?: return

        if (currentFieldCount >= MAX_FIELD_COUNT) {
            _messages.tryEmit("Feld ist voll ($MAX_FIELD_COUNT/$MAX_FIELD_COUNT) – zuerst einen Spieler auf die Bank (+ Bank)")
            return
        }

        _players.update { list ->
            list.map { if (it.id == playerId) it.copy(status = PlayerStatus.FIELD) else it }
        }
        _messages.tryEmit("${player.name} aufs Feld gestellt")
    }

    fun moveToBench(playerId: String) {
        val player = _players.value.find { it.id == playerId }
        _players.update { list ->
            list.map { if (it.id == playerId) it.copy(status = PlayerStatus.BENCH) else it }
        }
        if (player != null) {
            _messages.tryEmit("${player.name} auf die Bank gesetzt")
        }
    }

    fun addPlayer(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        val newPlayer = Player(
            id = "p_${System.currentTimeMillis()}",
            name = trimmed,
            status = PlayerStatus.BENCH
        )
        _players.update { it + newPlayer }
        _messages.tryEmit("Spieler \"$trimmed\" hinzugefügt")
    }

    fun deletePlayer(playerId: String) {
        val player = _players.value.find { it.id == playerId }
        _players.update { list -> list.filterNot { it.id == playerId } }
        if (player != null) {
            _messages.tryEmit("Spieler \"${player.name}\" entfernt")
        }
    }

    fun cycleTimeDisplayMode() {
        val next = when (_timeDisplayMode.value) {
            TimeDisplayMode.BOTH -> TimeDisplayMode.TIME
            TimeDisplayMode.TIME -> TimeDisplayMode.PERCENT
            TimeDisplayMode.PERCENT -> TimeDisplayMode.BOTH
        }
        _timeDisplayMode.value = next
        val label = when (next) {
            TimeDisplayMode.BOTH -> "Spielzeit: Min:Sek & Prozent"
            TimeDisplayMode.TIME -> "Spielzeit: Min:Sek (m:s)"
            TimeDisplayMode.PERCENT -> "Spielzeit: Prozentanteil (%)"
        }
        _messages.tryEmit(label)
    }

    fun toggleFullscreen() {
        _isFullscreen.update { !it }
    }

    fun setActiveTab(tab: TabId) {
        _activeTab.value = tab
    }
}
