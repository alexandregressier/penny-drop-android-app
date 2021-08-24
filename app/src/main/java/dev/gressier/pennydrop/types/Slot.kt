package dev.gressier.pennydrop.types

import dev.gressier.pennydrop.data.Game

data class Slot(
    val number: Int,
    val canBeFilled: Boolean = true,
    var isFilled: Boolean = false,
    var lastRolled: Boolean = false,
) {
    companion object {
        fun mapFromGame(game: Game?) =
            (1..6).map { n -> Slot(
                number = n,
                canBeFilled = n != 6,
                isFilled = game?.filledSlots?.contains(n) ?: false,
                lastRolled = game?.lastRoll == n,
            ) }
    }
}

fun List<Slot>.clear() =
    forEach { it.apply {
        isFilled = false
        lastRolled = false
    } }

fun List<Slot>.fullSlots(): Int =
    count { it.canBeFilled && it.isFilled }