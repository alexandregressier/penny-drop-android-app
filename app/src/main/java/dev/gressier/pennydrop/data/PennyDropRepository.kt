package dev.gressier.pennydrop.data

import androidx.lifecycle.LiveData
import dev.gressier.pennydrop.types.Player

class PennyDropRepository(private val pennyDropDao: PennyDropDao) {

    fun getCurrentGameWithPlayers(): LiveData<GameWithPlayers> =
        pennyDropDao.getCurrentGameWithPlayers()

    fun getCurrentGameStatuses(): LiveData<List<GameStatus>> =
        pennyDropDao.getCurrentGameStatuses()

    fun getCompletedGameStatusesWithPlayers(): LiveData<List<GameStatusWithPlayer>> =
        pennyDropDao.getCompletedGameStatusesWithPlayers()

    suspend fun startGame(players: List<Player>, pennyCount: Int?): Long =
        pennyDropDao.startGame(players, pennyCount)

    suspend fun updateGameAndStatuses(game: Game, statuses: List<GameStatus>) =
        pennyDropDao.updateGameAndStatuses(game, statuses)

    companion object {
        @Volatile private var instance: PennyDropRepository? = null

        fun getInstance(pennyDropDao: PennyDropDao) =
            instance ?: synchronized(this) {
                instance ?: PennyDropRepository(pennyDropDao)
                    .also { instance = it }
            }
    }
}