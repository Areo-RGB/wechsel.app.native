package com.example.model

enum class PlayerStatus {
    FIELD,
    BENCH
}

data class Player(
    val id: String,
    val name: String,
    val status: PlayerStatus,
    val feldzeit: Long = 0L, // seconds
    val bankzeit: Long = 0L  // seconds
) {
    val totalTime: Long
        get() = feldzeit + bankzeit

    fun getPercentage(matchElapsed: Long): Int {
        if (matchElapsed > 0) {
            val pct = (feldzeit.toDouble() / matchElapsed.toDouble()) * 100.0
            return pct.coerceIn(0.0, 100.0).toInt()
        }
        if (totalTime > 0) {
            val pct = (feldzeit.toDouble() / totalTime.toDouble()) * 100.0
            return pct.coerceIn(0.0, 100.0).toInt()
        }
        return 0
    }
}
