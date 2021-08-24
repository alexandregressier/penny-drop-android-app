package dev.gressier.pennydrop.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dev.gressier.pennydrop.data.GameState.FINISHED
import dev.gressier.pennydrop.data.GameState.STARTED
import dev.gressier.pennydrop.data.GameStatus
import dev.gressier.pennydrop.data.GameWithPlayers
import dev.gressier.pennydrop.data.PennyDropDatabase
import dev.gressier.pennydrop.data.PennyDropRepository
import dev.gressier.pennydrop.game.GameHandler
import dev.gressier.pennydrop.game.TurnEnd.BUST
import dev.gressier.pennydrop.game.TurnEnd.PASS
import dev.gressier.pennydrop.game.TurnResult
import dev.gressier.pennydrop.types.Player
import dev.gressier.pennydrop.types.Slot
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PennyDropRepository

    val currentGame = MediatorLiveData<GameWithPlayers>()

    val currentGameStatuses: LiveData<List<GameStatus>>
    val currentPlayer: LiveData<Player>
    val currentStandingsText: LiveData<String>

    val slots: LiveData<List<Slot>>

    val canRoll: LiveData<Boolean>
    val canPass: LiveData<Boolean>

    private var clearText: Boolean = false

    init {
        repository = PennyDropDatabase
            .getDatabase(application, viewModelScope)
            .pennyDropDao().let { PennyDropRepository.getInstance(it) }

        currentGameStatuses = repository.getCurrentGameStatuses()

        currentGame.apply {
            addSource(repository.getCurrentGameWithPlayers()) { gameWithPlayers ->
                updateCurrentGame(gameWithPlayers, currentGameStatuses.value)
            }
            addSource(currentGameStatuses) { gameStatuses ->
                updateCurrentGame(currentGame.value, gameStatuses)
            }
        }
        currentPlayer = Transformations.map(currentGame) { gameWithPlayers ->
            gameWithPlayers?.players?.firstOrNull { it.isRolling }
        }
        currentStandingsText = Transformations.map(currentGame) { gameWithPlayers ->
            gameWithPlayers?.players?.let { generateCurrentStandings(it) }
        }
        slots = Transformations.map(currentGame) { gameWithPlayers ->
            Slot.mapFromGame(gameWithPlayers?.game)
        }
        canRoll = Transformations.map(currentPlayer) { player ->
            player?.isHuman == true && currentGame.value?.game?.canRoll == true
        }
        canPass = Transformations.map(currentPlayer) { player ->
            player?.isHuman == true && currentGame.value?.game?.canPass == true
        }
    }

    private fun updateCurrentGame(gameWithPlayers: GameWithPlayers?, gameStatuses: List<GameStatus>?) {
        currentGame.value = gameWithPlayers?.updateStatuses(gameStatuses)
    }

    suspend fun startGame(playersForNewGame: List<Player>) {
        repository.startGame(playersForNewGame)
    }

    fun roll() {
        val game = currentGame.value?.game ?: return
        val players = currentGame.value?.players ?: return
        val currentPlayer = currentPlayer.value ?: return
        val slots = slots.value ?: return

        if (game.canRoll)
            updateFromGameHandler(GameHandler.roll(players, currentPlayer, slots))
    }

    fun pass() {
        val game = currentGame.value?.game ?: return
        val players = currentGame.value?.players ?: return
        val currentPlayer = currentPlayer.value ?: return

        if (game.canPass)
            updateFromGameHandler(GameHandler.pass(players, currentPlayer))
    }

    private fun updateFromGameHandler(result: TurnResult) {
        val game = currentGame.value?.let { curentGameWithPlayers ->
            curentGameWithPlayers.game.copy(
                gameState = if (result.isGameOver) FINISHED else STARTED,
                lastRoll = result.lastRoll,
                filledSlots = updateFilledSlots(result, curentGameWithPlayers.game.filledSlots),
                // This generates with old values since the game's yet to be updated
                currentTurnText = generateTurnText(result),
                canRoll = result.canRoll,
                canPass = result.canPass,
                endTime = if (result.isGameOver) OffsetDateTime.now() else null
            )
        } ?: return

        val statuses = currentGameStatuses.value?.map { status ->
            when (status.playerId) {
                result.previousPlayer?.playerId -> {
                    status.copy(
                        isRolling = false,
                        pennies = status.pennies + (result.coinChangeCount ?: 0),
                    )
                }
                result.currentPlayer?.playerId -> {
                    status.copy(
                        isRolling = !result.isGameOver,
                        pennies = status.pennies + if (!result.playerChanged) result.coinChangeCount ?: 0 else 0,
                    )
                }
                else -> status
            }
        } ?: emptyList()

        viewModelScope.launch {
            repository.updateGameAndStatuses(game, statuses)
            if (result.currentPlayer?.isHuman == false)
                playAITurn()
        }
    }

    private fun updateFilledSlots(result: TurnResult, filledSlots: List<Int>): List<Int> =
        when {
            result.clearSlots -> emptyList()
            result.lastRoll != null && result.lastRoll != 6 -> filledSlots + result.lastRoll
            else -> filledSlots
        }

    private fun generateTurnText(result: TurnResult): String {
        val currentText = if (clearText) "" else currentGame.value?.game?.currentTurnText ?: ""
        clearText = result.turnEnd != null

        val currentPlayerName = result.currentPlayer?.playerName ?: "???"

        return when {
            result.isGameOver -> generateGameOverText()
            result.turnEnd == BUST ->
                "${ohNoPhrases.random()} ${result.previousPlayer?.playerName} rolled a ${result.lastRoll}. They collected ${result.coinChangeCount} pennies for a total of ${result.previousPlayer?.pennies}.\n$currentText"
            result.turnEnd == PASS ->
                "${result.previousPlayer?.playerName} passed. They currently have ${result.previousPlayer?.pennies} pennies.\n$currentText"
            result.lastRoll != null ->
                "$currentPlayerName rolled a ${result.lastRoll}.\n$currentText"
            else -> ""
        }
    }

    private fun generateGameOverText(): String {
        val statuses = currentGameStatuses.value
        val players = currentGame.value?.players?.map { player ->
            player.apply {
                pennies = statuses
                    ?.firstOrNull { it.playerId == playerId }?.pennies
                    ?: Player.defaultPennyCount
            }
        }
        val winningPlayer = players
            ?.firstOrNull { !it.hasPenniesLeft() || it.isRolling }
            ?.apply { pennies = 0 }

        if (players == null || winningPlayer == null)
            return "N/A"

        return """Game Over!
                  |${winningPlayer.playerName} is the winner!
                  |
                  |${generateCurrentStandings(players, "Final Scores:\n")}
                  """.trimMargin()
    }

    private fun generateCurrentStandings(players: List<Player>, headerText: String = "Current Standings:") =
        players
            .sortedBy { it.pennies }
            .joinToString("\n", "$headerText\n") {
                "\t${it.playerName} - ${it.pennies} penn${if (it.pennies > 1) "ies" else "y"}"
            }

    private suspend fun playAITurn() {
        delay(1000)
        val game = currentGame.value?.game ?: return
        val players = currentGame.value?.players ?: return
        val currentPlayer = currentPlayer.value ?: return
        val slots = slots.value ?: return

        GameHandler.playAITurn(players, currentPlayer, slots, game.canPass)
            ?.also { updateFromGameHandler(it) }
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
}