package dev.gressier.pennydrop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gressier.pennydrop.game.GameHandler
import dev.gressier.pennydrop.game.TurnEnd.BUST
import dev.gressier.pennydrop.game.TurnEnd.PASS
import dev.gressier.pennydrop.game.TurnResult
import dev.gressier.pennydrop.types.Player
import dev.gressier.pennydrop.types.Slot
import dev.gressier.pennydrop.types.clear
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private var players: List<Player> = emptyList()

    val slots = MutableLiveData(
        (1..6).map { n -> Slot(number = n, canBeFilled = n != 6) }
    )
    val currentPlayer = MutableLiveData<Player?>()

    val canRoll = MutableLiveData(false)
    val canPass = MutableLiveData(false)

    val currentTurnText = MutableLiveData("")
    val currentStandingsText = MutableLiveData("")

    private var clearText: Boolean = false

    fun startGame(playersForNewGame: List<Player>) {
        players = playersForNewGame
        currentPlayer.value = players.firstOrNull().also { it?.isRolling = true }

        canRoll.value = true
        canPass.value = true

        slots.value?.clear()
        slots.notifyChange()

        currentTurnText.value = "The game has begun!\n"
        currentStandingsText.value = generateCurrentStandings(players)
    }

    fun roll() {
        slots.value?.let { currentSlots ->
            players.firstOrNull { it.isRolling }?.let { currentPlayer ->
                if (canRoll.value == true)
                    updateFromGameHandler(GameHandler.roll(players, currentPlayer, currentSlots))
        } } }

    fun pass() {
        players.firstOrNull { it.isRolling }?.let { currentPlayer ->
            if (canRoll.value == true)
                updateFromGameHandler(GameHandler.pass(players, currentPlayer))
        } }

    private fun generateTurnText(result: TurnResult): String {
        if (clearText)
            currentTurnText.value = ""
        clearText = result.turnEnd != null

        val currentText = currentTurnText.value ?: ""
        val currentPlayerName = result.currentPlayer?.playerName ?: "???"

        return when {
            result.isGameOver ->
                """Game Over!
                  |$currentPlayerName is the winner!
                  |
                  |${generateCurrentStandings(players, "Final Scores:\n")}
                  """.trimMargin()
            result.turnEnd == BUST ->
                "${ohNoPhrases.random()} ${result.previousPlayer?.playerName} rolled a ${result.lastRoll}. They collected ${result.coinChangeCount} pennies for a total of ${result.previousPlayer?.pennies}.\n$currentText"
            result.turnEnd == PASS ->
                "${result.previousPlayer?.playerName} passed.  They currently have ${result.previousPlayer?.pennies} pennies.\n$currentText"
            result.lastRoll != null ->
                "$currentPlayerName rolled a ${result.lastRoll}.\n$currentText"
            else -> ""
        }
    }

    private fun generateCurrentStandings(players: List<Player>, headerText: String = "Current Standings:") =
        players.sortedBy { it.pennies }.joinToString("\n", "$headerText\n") {
            "\t${it.playerName} - ${it.pennies} penn${if (it.pennies > 1) "ies" else "y"}"
        }

    private fun updateFromGameHandler(result: TurnResult) {
        result.currentPlayer?.let { resultCurrentPlayer ->
            currentPlayer.value?.addPennies(result.coinChangeCount ?: 0)
            currentPlayer.value = resultCurrentPlayer
            players.forEach { player ->
                player.isRolling = resultCurrentPlayer == player
            }
        }
        result.lastRoll?.let {
            slots.value?.let { currentSlots ->
                updateSlots(result, currentSlots, result.lastRoll)
            }
        }
        currentTurnText.value = generateTurnText(result)
        currentStandingsText.value = generateCurrentStandings(players)

        canRoll.value = result.canRoll
        canPass.value = result.canPass

        if (!result.isGameOver && result.currentPlayer?.isHuman == false) {
            canRoll.value = false
            canPass.value = false
            playAITurn()
        }
    }

    private fun updateSlots(result: TurnResult, currentSlots: List<Slot>, lastRoll: Int) {
        if (result.clearSlots)
            currentSlots.clear()

        currentSlots.firstOrNull { it.lastRolled }?.apply { lastRolled = false }

        currentSlots.getOrNull(lastRoll - 1)?.also { slot ->
            if (!result.clearSlots && slot.canBeFilled)
                slot.isFilled = true
            slot.lastRolled = true
        }
        slots.notifyChange()
    }

    private fun playAITurn() {
        viewModelScope.launch {
            delay(1000)
            slots.value?.let { slots ->
                players.firstOrNull { it.isRolling }?.let { player ->
                    if (!player.isHuman)
                        GameHandler.playAITurn(players, player, slots, canPass.value == true)
                            ?.also { updateFromGameHandler(it) }
                }
            }
        }
    }

    private val ohNoPhrases = listOf(
        "Oh no!",
        "Bummer!",
        "Dang.",
        "Whoops.",
        "Ah, fiddlesticks.",
        "Oh, kitty cats.",
        "Piffle.",
        "Well, crud.",
        "Ah, cinnamon bits.",
        "Ooh, bad luck.",
        "Shucks!",
        "Woopsie daisy.",
        "Nooooooo!",
        "Aw, rats and bats.",
        "Blood and thunder!",
        "Gee whillikins.",
        "Well that's disappointing.",
        "I find your lack of luck disturbing.",
        "That stunk, huh?",
        "Uff da."
    )

    private fun <T> MutableLiveData<List<T>>.notifyChange() {
        value = value
    }
}