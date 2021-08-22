package dev.gressier.pennydrop.game

import dev.gressier.pennydrop.game.TurnEnd.*
import dev.gressier.pennydrop.types.Player
import dev.gressier.pennydrop.types.Slot

object GameHandler {

    fun roll(players: List<Player>, currentPlayer: Player, slots: List<Slot>): TurnResult =
        rollDie().let { lastRoll ->
            slots.getOrNull(lastRoll - 1)?.let { slot ->
                if (slot.isFilled) {
                    TurnResult(
                        lastRoll,
                        coinChangeCount = slots.count { it.isFilled },
                        clearSlots = true,
                        turnEnd = BUST,
                        previousPlayer = currentPlayer,
                        currentPlayer = nextPlayer(players, currentPlayer),
                        playerChanged = true,
                        canRoll = true,
                        canPass = false,
                    )
                } else {
                    if (!currentPlayer.hasPenniesLeft(true)) {
                        TurnResult(
                            lastRoll,
                            currentPlayer = currentPlayer,
                            coinChangeCount = -1,
                            isGameOver = true,
                            turnEnd = WIN,
                            canRoll = false,
                            canPass = false,
                        )
                    } else {
                        TurnResult(
                            lastRoll,
                            currentPlayer = currentPlayer,
                            canRoll = true,
                            canPass = true,
                            coinChangeCount = -1,
                        )
                    }
                }
            } ?: TurnResult(isGameOver = true)
        }

    fun pass(players: List<Player>, currentPlayer: Player): TurnResult =
        TurnResult(
            previousPlayer = currentPlayer,
            currentPlayer = nextPlayer(players, currentPlayer),
            playerChanged = true,
            turnEnd = PASS,
            canRoll = true,
            canPass = false,
        )

    private fun rollDie(sides: Int = 6): Int =
        (1..sides).random()

    private fun nextPlayer(players: List<Player>, currentPlayer: Player): Player =
        players[(players.indexOf(currentPlayer) + 1) % players.size]

    fun playAITurn(players: List<Player>, currentPlayer: Player, slots: List<Slot>, canPass: Boolean): TurnResult? =
        currentPlayer.selectedAI?.run {
            if (!canPass || rollAgain(slots))
                roll(players, currentPlayer, slots)
            else
                pass(players, currentPlayer)
        }
}