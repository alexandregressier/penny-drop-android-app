package dev.gressier.pennydrop.data

import androidx.lifecycle.LiveData
import dev.gressier.pennydrop.types.Player

class PennyDropRepository(private val pennyDropDao: PennyDropDao) {

    fun getCurrentGameWithPlayers(): LiveData<GameWithPlayers> =
        pennyDropDao.getCurrentGameWithPlayers()

    fun getCurrentGameStatuses(): LiveData<List<GameStatus>> =
        pennyDropDao.getCurrentGameStatuses()

    suspend fun startGame(players: List<Player>): Long =
        pennyDropDao.startGame(players)

    suspend fun updateGameAndStatuses(game: Game, status: List<GameStatus>) =
        pennyDropDao.updateGameAndStatuses(game, status)

    companion object {

        @Volatile private var instance: PennyDropRepository? = null

        fun getInstance(pennyDropDao: PennyDropDao) =
            instance ?: synchronized(this) {
                instance ?: PennyDropRepository(pennyDropDao)
                    .also { instance = it }
            }
    }
}