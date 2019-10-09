package com.github.michalchojnacki.findbook.ui.navigation

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.permission.PermissionRequester
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.data.di.MockSearchForBooksService
import com.github.michalchojnacki.findbook.ui.di.InjectorProvider
import com.github.michalchojnacki.findbook.ui.helpers.CustomMatchers
import com.github.michalchojnacki.findbook.ui.helpers.WaitPeriod
import com.github.michalchojnacki.findbook.ui.helpers.wait
import io.mockk.coEvery
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class MainNavigationActivityTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainNavigationActivity::class.java, false)
    private val mockSearchForBooksService =
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as InjectorProvider).component.searchForBooksService as MockSearchForBooksService

    @Before
    fun setUp() {
        PermissionRequester().apply {
            addPermissions(android.Manifest.permission.CAMERA)
            requestPermissions()
        }
    }

    @Test
    fun integrationTest() {
        wait(WaitPeriod.SHORT)
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(any()) }.returns(Response.success(mockSearchForBooksService.searchForBooksWithQuerySuccessfulResponseBody))
        coEvery { mockSearchForBooksService.getBookDetails(any()) }.returns(Response.success(mockSearchForBooksService.getBookDetailsSuccessfulResponseBody))

        Espresso.onView(withId(R.id.scannerGoToSearchTextBtn)).check(matches(isDisplayed()))
            .perform(ViewActions.click())
        wait(WaitPeriod.SHORT)

        Espresso.onView(withId(R.id.typingSearchWithTextEt)).check(matches(isDisplayed()))
            .perform(ViewActions.typeText("fake query")).perform(closeSoftKeyboard())
        wait(WaitPeriod.SHORT)
        Espresso.onView(withId(R.id.typingSearchWithTextBtn)).check(matches(isDisplayed()))
            .perform(ViewActions.click())
        wait(WaitPeriod.SHORT)

        Espresso.onView(withId(R.id.books_rv)).check(matches(isDisplayed()))
        Espresso.onView(CustomMatchers.withIndex(withId(R.id.bookListItemTitle), 0)).perform(ViewActions.click())
        wait(WaitPeriod.SHORT)

        Espresso.onView(withId(R.id.bookDetailsShowComments)).check(matches(isDisplayed())).perform(ViewActions.click())
        wait(WaitPeriod.SHORT)

        Espresso.onView(withId(R.id.bookDetailsReviewsWebview)).check(matches(isDisplayed()))
    }
}