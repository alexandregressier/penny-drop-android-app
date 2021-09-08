package dev.gressier.pennydrop

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matchers.allOf

fun typeInPlayerName(parentId: Int, text: String) {
    onView(
        allOf(
            withId(R.id.edit_text_player_name),
            withParent(withId(parentId))
        )
    ).perform(typeText(text))
}

fun clickPlayerCheckbox(parentId: Int) {
    onView(
        allOf(
            withId(R.id.checkbox_player_active),
            withParent(withId(parentId))
        )
    ).perform(click())
}