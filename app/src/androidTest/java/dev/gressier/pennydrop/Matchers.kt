package dev.gressier.pennydrop

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun isActivated() = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("The View is activated")
    }

    override fun matchesSafely(view: View?): Boolean =
        view?.isActivated == true
}