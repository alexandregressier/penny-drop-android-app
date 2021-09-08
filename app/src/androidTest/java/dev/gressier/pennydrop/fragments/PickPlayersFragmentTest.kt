package dev.gressier.pennydrop.fragments

import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import dev.gressier.pennydrop.MainActivity
import dev.gressier.pennydrop.R
import dev.gressier.pennydrop.clickPlayerCheckbox
import dev.gressier.pennydrop.game.AI
import dev.gressier.pennydrop.typeInPlayerName
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PickPlayersFragmentTest {

    @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()

    @Before fun goToPickPlayersFragment() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.findNavController(R.id.containerFragment).navigate(R.id.pickPlayersFragment)
        }
    }

    @Test fun Test_finding_FAB() {
        onView(withId(R.id.buttonPlayGame)).check(matches(isDisplayed()))
    }

    @Test fun Test_adding_named_players() {
        typeInPlayerName(R.id.mainPlayer, "Alex")
        typeInPlayerName(R.id.player2, "Sofiane")
        closeSoftKeyboard()
        onView(withId(R.id.buttonPlayGame)).perform(click())

        onView(withId(R.id.textCurrentPlayerName)).check(matches(withText("Alex")))
        onView(withId(R.id.textCurrentStandingsInfo)).check(
            matches(
                allOf(
                    withText(containsString("Alex - 10 pennies")),
                    withText(containsString("Sofiane - 10 pennies")),
                )
            )
        )
    }

    @Test fun Test_adding_3_named_players() {
        typeInPlayerName(R.id.mainPlayer, "Alex")
        typeInPlayerName(R.id.player2, "Sofiane")
        clickPlayerCheckbox(R.id.player3)
        typeInPlayerName(R.id.player3, "Adrien")
        closeSoftKeyboard()
        onView(withId(R.id.buttonPlayGame)).perform(click())

        onView(withId(R.id.textCurrentPlayerName)).check(matches(withText("Alex")))
        onView(withId(R.id.textCurrentStandingsInfo)).check(
            matches(
                allOf(
                    withText(containsString("Alex - 10 pennies")),
                    withText(containsString("Sofiane - 10 pennies")),
                    withText(containsString("Adrien - 10 pennies")),
                )
            )
        )
    }

    @Test fun Test_adding_3rd_player_as_AI() {
        typeInPlayerName(R.id.mainPlayer, "Alex")
        typeInPlayerName(R.id.player2, "Sofiane")
        closeSoftKeyboard()

        clickPlayerCheckbox(R.id.player3)
        onView(
            allOf(
                withId(R.id.switch_player_type),
                withParent(withId(R.id.player3)),
            )
        ).perform(click())
        onView(
            allOf(
                withId(R.id.spinner_ai_name),
                withParent(withId(R.id.player3)),
            )
        ).perform(click())
        onData(`is`(instanceOf(AI::class.java))).atPosition(3).perform(click()) // "Fearful Fred"

        onView(withId(R.id.buttonPlayGame)).perform(click())

        onView(withId(R.id.textCurrentPlayerName)).check(matches(withText("Alex")))
        onView(withId(R.id.textCurrentStandingsInfo)).check(
            matches(
                allOf(
                    withText(containsString("Alex - 10 pennies")),
                    withText(containsString("Sofiane - 10 pennies")),
                    withText(containsString("Fearful Fred - 10 pennies")),
                )
            )
        )
    }

}