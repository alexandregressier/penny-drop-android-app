package dev.gressier.pennydrop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.gressier.pennydrop.types.Player
import dev.gressier.pennydrop.types.Slot

class GameViewModel : ViewModel() {

    private var players: List<Player> = emptyList()

    val slots = MutableLiveData(
        (1..6).map { Slot(number = it, canBeFilled = it != 6) }
    )
    val currentPlayer = MutableLiveData<Player?>()
    val canRoll = MutableLiveData(false)
    val canPass = MutableLiveData(false)
    val currentTurnText = MutableLiveData("")
    val currentStandingsText = MutableLiveData("")

    fun startGame(playersForNewGame: List<Player>) {
        players = playersForNewGame
        currentPlayer.value = players.firstOrNull().apply { this?.isRolling = true }
        canRoll.value = true
    }

    fun roll() {
        TODO()
    }

    fun pass() {
        TODO()
    }
}