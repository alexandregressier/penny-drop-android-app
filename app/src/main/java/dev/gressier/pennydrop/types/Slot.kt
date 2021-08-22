package dev.gressier.pennydrop.types

data class Slot(
    val number: Int,
    val canBeFilled: Boolean = true,
    var isFilled: Boolean = false,
    var lastRolled: Boolean = false,
)

fun List<Slot>.clear() =
    forEach { it.apply {
        isFilled = false
        lastRolled = false
    } }

fun List<Slot>.fullSlots(): Int =
    count { it.canBeFilled && it.isFilled }