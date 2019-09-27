package com.github.michalchojnacki.findbook.ui.navigation

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.permission.PermissionRequester
import com.github.michalchojnacki.findbook.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainNavigationActivityTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainNavigationActivity::class.java, false)

    @Before
    fun setUp() {
        PermissionRequester().apply {
            addPermissions(android.Manifest.permission.CAMERA)
            requestPermissions()
        }
    }

    @Test
    fun integrationTest() {
        Espresso.onView(withId(R.id.scannerGoToSearchTextBtn)).check(matches(isDisplayed()))
            .perform(ViewActions.click())
        runBlocking { delay(1_000) }

        Espresso.onView(withId(R.id.typingSearchWithTextEt)).check(matches(isDisplayed()))
            .perform(ViewActions.typeText("fake query"))
        Espresso.onView(withId(R.id.typingSearchWithTextBtn)).check(matches(isDisplayed()))
            .perform(ViewActions.click())
        runBlocking { delay(1_000) }

        Espresso.onView(withId(R.id.books_rv)).check(matches(isDisplayed()))
    }
}