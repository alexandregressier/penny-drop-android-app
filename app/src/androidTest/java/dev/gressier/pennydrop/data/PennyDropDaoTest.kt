package dev.gressier.pennydrop.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.gressier.pennydrop.game.AI
import dev.gressier.pennydrop.getOrAwaitValue
import dev.gressier.pennydrop.types.Player
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class PennyDropDaoTest {

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: PennyDropDatabase
    private lateinit var dao: PennyDropDao

    @Before fun initDatabaseAndDao() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PennyDropDatabase::class.java,
        )
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        dao = database.pennyDropDao()
    }

    @After fun closeDatabase() =
        database.close()

    @Test fun Test_inserting_a_new_player() =
        runBlocking {
            val player = Player(5, "Hazel")
            assertNull(dao.getPlayer(player.playerName))

            val insertedPlayerId = dao.insertPlayer(player)
            assertEquals(player.playerId, insertedPlayerId)

            dao.getPlayer(player.playerName)?.let { newPlayer ->
                assertEquals(player.playerId, newPlayer.playerId)
                assertEquals(player.playerName, newPlayer.playerName)
                assertTrue(player.isHuman)
            } ?: fail("New player not found")
        }

    @Test fun Test_starting_a_new_game() =
        runBlocking {
            val players = listOf(
                Player(23, "Alex"),
                Player(12, "Sofiane"),
                Player(5, "Adrien"),
                Player(100, "Even Steven", isHuman = false, selectedAI = AI.basicAI[5]),
            )
            val pennyCount = 15
            val gameId = dao.startGame(players, pennyCount)

            dao.getCurrentGameWithPlayers().getOrAwaitValue()?.let { gameWithPlayers ->
                gameWithPlayers.game.apply {
                    assertEquals(gameId, this.gameId)
                    assertNotNull(startTime)
                    assertNull(endTime)
                    assertNull(lastRoll)
                    assertTrue(canRoll)
                    assertFalse(canPass)
                }
                gameWithPlayers.players.forEach { player ->
                    assertTrue(players.contains(player))
                }
            } ?: fail("No current game with players found")

            players.map { it.playerName }.forEach { playerName ->
                assertNotNull(dao.getPlayer(playerName))
            }
            val playerIds = players.map { it.playerId }
            dao.getCurrentGameStatuses().getOrAwaitValue()?.let { gameStatuses ->
                assertTrue(gameStatuses.all { it.gameId == gameId })
                assertTrue(gameStatuses.all { playerIds.contains(it.playerId) })
                assertTrue(gameStatuses.all { it.pennies == pennyCount })
                assertEquals(1, gameStatuses.count { it.isRolling })
                assertEquals(players.first().playerId, gameStatuses.first { it.isRolling }.playerId)
            } ?: fail("No current game with players found")
        }
}