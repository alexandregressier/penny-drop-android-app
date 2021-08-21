package dev.gressier.pennydrop.types

import androidx.databinding.ObservableBoolean
import dev.gressier.pennydrop.game.AI

data class NewPlayer(
    var playerName: String = "",
    val isHuman: ObservableBoolean = ObservableBoolean(true),
    val canBeRemoved: Boolean = true,
    val canBeToggled: Boolean = true,
    var isIncluded: ObservableBoolean = ObservableBoolean(!canBeRemoved),
    var selectedAIPosition: Int = -1
) {
    fun selectedAI(): AI? =
        takeUnless { isHuman.get() }?.let { AI.basicAI.getOrNull(selectedAIPosition) }

    fun toPlayer(): Player =
        Player(
            playerName = if (isHuman.get()) playerName else selectedAI()?.name ?: "AI",
            isHuman = isHuman.get(),
            selectedAI = selectedAI(),
        )
}