package dev.gressier.pennydrop.data

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.gressier.pennydrop.data.GameState.CANCELLED
import dev.gressier.pennydrop.data.GameState.STARTED
import dev.gressier.pennydrop.types.Player
import java.time.OffsetDateTime

@Dao
abstract class PennyDropDao {

    @Query("SELECT * FROM players WHERE playerName = :playerName")
    abstract fun getPlayer(playerName: String): Player?

    @Transaction
    @Query("SELECT * FROM games ORDER BY startTime DESC LIMIT 1")
    abstract fun getCurrentGameWithPlayers(): LiveData<GameWithPlayers>

    @Transaction
    @Query("""
        SELECT * FROM game_statuses
        WHERE gameId = (
            SELECT gameId FROM games
            WHERE endTime IS NULL
            ORDER BY startTime DESC
            LIMIT 1)
        ORDER BY gamePlayerNumber""")
    abstract fun getCurrentGameStatuses(): LiveData<List<GameStatus>>

    @Insert
    abstract suspend fun insertGame(game: Game): Long

    @Insert
    abstract suspend fun insertPlayer(player: Player): Long

    @Insert
    abstract suspend fun insertPlayers(players: List<Player>): List<Long>

    @Insert
    abstract suspend fun insertGameStatuses(gameStatuses: List<GameStatus>)

    @Update
    abstract suspend fun updateGame(game: Game)

    @Update
    abstract suspend fun updateGameStatuses(gameStatuses: List<GameStatus>)

    @Query("""
        UPDATE games
        SET endTime = :endDate, gameState = :gameState
        WHERE endTime IS NULL""")
    abstract suspend fun closeOpenGames(
        endDate: OffsetDateTime = OffsetDateTime.now(),
        gameState: GameState = CANCELLED,
    )

    @Transaction
    open suspend fun startGame(players: List<Player>): Long {
        closeOpenGames()
        val gameId = insertGame(
            Game(
                gameState = STARTED,
                currentTurnText = "The game has begun!\n",
                canRoll = true,
            )
        )
        insertGameStatuses(
            players
                .map { getPlayer(it.playerName)?.playerId ?: insertPlayer(it) }
                .mapIndexed { i, playerId -> GameStatus(gameId, playerId, i, i == 0) }
        )
        return gameId
    }

    @Transaction
    open suspend fun updateGameAndStatuses(game: Game, status: List<GameStatus>) {
        updateGame(game)
        updateGameStatuses(status)
    }
}