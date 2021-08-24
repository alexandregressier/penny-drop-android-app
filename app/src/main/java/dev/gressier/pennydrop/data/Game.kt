package dev.gressier.pennydrop.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.gressier.pennydrop.data.GameState.STARTED
import java.time.OffsetDateTime

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true) var gameId: Long = 0,
    var gameState: GameState = STARTED,
    val startTime: OffsetDateTime? = OffsetDateTime.now(),
    val endTime: OffsetDateTime? = null,
    val filledSlots: List<Int> = emptyList(),
    val lastRoll: Int? = null,
    val currentTurnText: String? = null,
    val canRoll: Boolean = false,
    val canPass: Boolean = false,
)

enum class GameState { STARTED, FINISHED, CANCELLED, UNKNOWN }