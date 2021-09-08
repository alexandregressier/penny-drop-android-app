package dev.gressier.pennydrop.game

import dev.gressier.pennydrop.game.TurnEnd.PASS
import dev.gressier.pennydrop.types.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test

class GameHandlerTest {

    companion object {
        private lateinit var testPlayers: List<Player>

        @BeforeClass @JvmStatic fun initTestPlayers() {
            testPlayers = listOf(
                Player(playerName = "Alex"),
                Player(playerName = "Sofiane"),
                Player(playerName = "Adrien"),
                Player(playerName = "Riverboat Ron", isHuman = false, selectedAI = AI.basicAI[5]),
            )
        }
    }

    @Test fun `Test nextPlayer() via pass() function`() {
        checkNextPlayer(
            findPlayerBy(name = "Sofiane"),
            findPlayerBy(name = "Adrien"),
        )
    }

    @Test fun `Test last nextPlayer() via pass() function`() {
        checkNextPlayer(
            findPlayerBy(name = "Riverboat Ron"),
            findPlayerBy(name = "Alex"),
        )
    }

    private fun findPlayerBy(name: String): Player =
        testPlayers.first { it.playerName == name }

    private fun checkNextPlayer(currentPlayer: Player, nextPlayer: Player) {
        GameHandler.pass(testPlayers, currentPlayer).also { result ->
            assertTrue(result.playerChanged)
            assertEquals(PASS, result.turnEnd)
            assertEquals(currentPlayer, result.previousPlayer)
            assertEquals(nextPlayer, result.currentPlayer)
        }
    }
}