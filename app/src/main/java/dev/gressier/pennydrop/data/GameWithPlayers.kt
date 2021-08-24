package dev.gressier.pennydrop.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.gressier.pennydrop.types.Player

data class GameWithPlayers(
    @Embedded val game: Game,
    @Relation(parentColumn = "gameId", entityColumn = "playerId", associateBy = Junction(GameStatus::class))
    val players: List<Player>,
) {
    fun updateStatuses(gameStatuses: List<GameStatus>?): GameWithPlayers =
        gameStatuses?.let {
            copy(players = players.map { player ->
                gameStatuses.firstOrNull { it.playerId == player.playerId }
                    ?.let { gameStatus ->
                        player.apply {
                            pennies = gameStatus.pennies
                            isRolling = gameStatus.isRolling
                            gamePlayerNumber = gameStatus.gamePlayerNumber
                        }
                    } ?: player
            }.sortedBy { it.gamePlayerNumber })
        } ?: this
}