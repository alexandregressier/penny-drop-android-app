package dev.gressier.pennydrop.game

import dev.gressier.pennydrop.game.TurnEnd.PASS
import dev.gressier.pennydrop.types.Player
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test

class GameHandlerTest {

    @Test
    fun `Test nextPlayer() via pass() function`() {
        val currentPlayer = testPlayers.first { it.playerName == "Sofiane" }
        val nextPlayer = testPlayers.first { it.playerName == "Adrien" }
        checkNextPlayer(currentPlayer, nextPlayer)
    }

    @Test
    fun `Test last nextPlayer() via pass() function`() {
        val currentPlayer = testPlayers.first { it.playerName == "Riverboat Ron" }
        val nextPlayer = testPlayers.first { it.playerName == "Alex" }
        checkNextPlayer(currentPlayer, nextPlayer)
    }

    private fun checkNextPlayer(currentPlayer: Player, nextPlayer: Player) {
        GameHandler.pass(testPlayers, currentPlayer).also { result ->
            assertTrue(result.playerChanged)
            assertEquals(PASS, result.turnEnd)
            assertEquals(currentPlayer, result.previousPlayer)
            assertEquals(nextPlayer, result.currentPlayer)
        }
    }

    companion object {
        private lateinit var testPlayers: List<Player>

        @BeforeClass
        @JvmStatic
        fun setUpTestPlayers() {
            testPlayers = listOf(
                Player(playerName = "Alex"),
                Player(playerName = "Sofiane"),
                Player(playerName = "Adrien"),
                Player(playerName = "Riverboat Ron", isHuman = false, selectedAI = AI.basicAI[5]),
            )
        }
    }
}