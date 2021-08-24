package dev.gressier.pennydrop.types

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.gressier.pennydrop.game.AI

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true) var playerId: Long = 0,
    val playerName: String = "",
    val isHuman: Boolean = true,
    val selectedAI: AI? = null,
) {
    @Ignore var pennies: Int = defaultPennyCount
    @Ignore var isRolling: Boolean = false
    @Ignore var gamePlayerNumber: Int = -1

    fun hasPenniesLeft(subtractPenny: Boolean = false): Boolean =
        (pennies - subtractPenny.compareTo(false)) > 0

    companion object {
        const val defaultPennyCount = 10
    }
}