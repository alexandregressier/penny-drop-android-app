package dev.gressier.pennydrop.game

import dev.gressier.pennydrop.types.Player
import dev.gressier.pennydrop.types.Slot
import dev.gressier.pennydrop.types.fullSlots

data class AI(
    val aiId: Long = 0,
    val name: String,
    val rollAgain: (slots: List<Slot>) -> Boolean,
) {
    fun toPlayer(): Player =
        Player(
            playerId = aiId,
            playerName = name,
            isHuman = false,
            selectedAI = this,
        )

    override fun toString() = name

    companion object {
        @JvmStatic val basicAI = listOf(
            AI(1, "TwoFace") { it.fullSlots() < 3 || (it.fullSlots() == 3 && coinFlipIsHeads()) },
            AI(2, "No Go Noah") { it.fullSlots() == 0 },
            AI(3, "Bail Out Beulah") { it.fullSlots() <= 1 },
            AI(4, "Fearful Fred") { it.fullSlots() <= 2 },
            AI(5, "Even Steven") { it.fullSlots() <= 3 },
            AI(6, "Riverboat Ron") { it.fullSlots() <= 4 },
            AI(7, "Sammy Sixes") { it.fullSlots() <= 5 },
            AI(8, "Random Rachael") { coinFlipIsHeads() },
        )
    }
}

fun coinFlipIsHeads() =
    (0..1).random() == 0