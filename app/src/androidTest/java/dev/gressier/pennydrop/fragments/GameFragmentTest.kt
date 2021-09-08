package dev.gressier.pennydrop.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import dev.gressier.pennydrop.MainActivity
import dev.gressier.pennydrop.R
import dev.gressier.pennydrop.startSampleGame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.*
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import dev.gressier.pennydrop.isActivated as isLastRolled

@ExperimentalCoroutinesApi
class GameFragmentTest {

    @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()

    private val coinSlotMap = listOf(
        R.id.coinSlot1, R.id.coinSlot2, R.id.coinSlot3, R.id.coinSlot4, R.id.coinSlot5, R.id.coinSlot6,
    )
        .mapIndexed { i, it -> "${i + 1}" to it }
        .toMap()

    @Before fun startNewGame() =
        runBlockingTest {
            startSampleGame(activityScenarioRule.scenario)
        }

    @Test fun Check_starting_slots() {
        coinSlotMap.forEach { slotNumber, slotId ->
            onView(
                allOf(withId(R.id.slotNumberCoinSlot), withParent(withId(slotId)))
            ).check(matches(withText(slotNumber)))

            onView(
                allOf(withId(R.id.coinImageCoinSlot), withParent(withId(slotId)))
            ).check(matches(not(isDisplayed())))

            if (slotId != R.id.coinSlot6)
                onView(withId(R.id.coinSlot6)).check(isCompletelyBelow(withId(slotId)))
        }
    }

    @Test fun Check_single_roll_result() =
        runBlockingTest {
            onView(withId(R.id.rollButton)).perform(click())

            onView(withId(R.id.textCurrentPlayerName)).check(matches(withText("Alex")))
            onView(withId(R.id.textCurrentPlayerCoinsLeft)).check(matches(withText("9")))

            onView(
                allOf(
                    withId(R.id.bottomViewCoinSlot),
                    isLastRolled(),
                    anyOf(
                        hasSibling(allOf(withId(R.id.coinImageCoinSlot), isDisplayed())),
                        hasSibling(allOf(withId(R.id.slotNumberCoinSlot), withText("6"))),
                    ),
                )
            ).check { view, noViewFoundException ->
                assertNull(noViewFoundException)
                assertNotNull(view)
            }
        }
}