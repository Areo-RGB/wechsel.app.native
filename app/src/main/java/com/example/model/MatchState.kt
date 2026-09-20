package com.example.model

enum class MatchStatus {
    IDLE,
    RUNNING,
    PAUSED
}

enum class TimeDisplayMode {
    BOTH,
    TIME,
    PERCENT
}

enum class TabId {
    KADER,
    AUFSTELLUNG,
    DATEN
}

data class MatchState(
    val status: MatchStatus = MatchStatus.IDLE,
    val elapsed: Long = 0L // seconds
)
