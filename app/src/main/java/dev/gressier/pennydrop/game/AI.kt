package dev.gressier.pennydrop.game

import dev.gressier.pennydrop.types.Slot
import dev.gressier.pennydrop.types.fullSlots

data class AI(
    val name: String,
    val rollAgain: (slots: List<Slot>) -> Boolean,
) {
    override fun toString() = name

    companion object {
        @JvmStatic val basicAI = listOf(
            AI("TwoFace") { it.fullSlots() < 3 || (it.fullSlots() == 3 && coinFlipIsHeads()) },
            AI("No Go Noah") { it.fullSlots() == 0 },
            AI("Bail Out Beulah") { it.fullSlots() <= 1 },
            AI("Fearful Fred") { it.fullSlots() <= 2 },
            AI("Even Steven") { it.fullSlots() <= 3 },
            AI("Riverboat Ron") { it.fullSlots() <= 4 },
            AI("Sammy Sixes") { it.fullSlots() <= 5 },
            AI("Random Rachael") { coinFlipIsHeads() },
        )
    }
}

fun coinFlipIsHeads() =
    (0..1).random() == 0