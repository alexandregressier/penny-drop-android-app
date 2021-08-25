package dev.gressier.pennydrop.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dev.gressier.pennydrop.data.PennyDropDatabase
import dev.gressier.pennydrop.data.PennyDropRepository
import dev.gressier.pennydrop.types.PlayerSummary

class RankingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PennyDropRepository

    val playerSummaries: LiveData<List<PlayerSummary>>

    init {
        repository = PennyDropDatabase
            .getDatabase(application, viewModelScope)
            .pennyDropDao().let { PennyDropRepository.getInstance(it) }

        playerSummaries = Transformations.map(repository.getCompletedGameStatusesWithPlayers()) { statusesWithPlayers ->
            statusesWithPlayers
                .groupBy { it.player }
                .map { (player, statuses) ->
                    PlayerSummary(
                        id = player.playerId,
                        name = player.playerName,
                        gamesPlayed = statuses.count(),
                        wins = statuses.count { it.gameStatus.pennies == 0 },
                        isHuman = player.isHuman,
                    )
                }
                .sortedWith(compareBy({ -it.wins }, { -it.gamesPlayed }))
        }
    }
}