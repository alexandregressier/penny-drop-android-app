package dev.gressier.pennydrop.types

import dev.gressier.pennydrop.game.AI

data class Player(
    val playerName: String = "",
    val isHuman: Boolean = true,
    val selectedAI: AI? = null,
) {
    var pennies: Int = defaultPennyCount

    fun addPennies(count: Int = 1) {
        pennies += count
    }

    fun hasPenniesLeft(subtractPenny: Boolean = false): Boolean =
        (pennies - subtractPenny.compareTo(false)) > 0

    var isRolling: Boolean = false

    companion object {
        const val defaultPennyCount = 10
    }
}