package dev.gressier.pennydrop.data

import androidx.room.Embedded
import androidx.room.Relation
import dev.gressier.pennydrop.types.Player

data class GameStatusWithPlayer(
    @Embedded val gameStatus: GameStatus,
    @Relation(parentColumn = "playerId", entityColumn = "playerId")
    val player: Player,
)
