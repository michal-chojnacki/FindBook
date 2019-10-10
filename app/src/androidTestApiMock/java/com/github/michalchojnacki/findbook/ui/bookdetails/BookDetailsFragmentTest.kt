package com.github.michalchojnacki.findbook.ui.bookdetails

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.data.di.MockSearchForBooksService
import com.github.michalchojnacki.findbook.ui.di.InjectorProvider
import com.github.michalchojnacki.findbook.ui.helpers.WaitPeriod
import com.github.michalchojnacki.findbook.ui.helpers.wait
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BookDetailsFragmentTest {
    private val mockSearchForBooksService =
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as InjectorProvider).component.searchForBooksService as MockSearchForBooksService

    @Before
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun testLoadingBookDetails_happyCaseScenario() {
        val fakeResponse = mockSearchForBooksService.getBookDetailsSuccessfulResponseBody
        val fakeResponseBookId = fakeResponse.book!!.id!!
        coEvery { mockSearchForBooksService.getBookDetails(fakeResponseBookId) }.returns(
            Response.success(fakeResponse)
        )

        launchFragmentInContainer<BookDetailsFragment>(
            BookDetailsFragment.newInstance(
                fakeResponseBookId,
                fakeResponse.book!!.title!!
            ).arguments,
            themeResId = R.style.AppTheme
        )

        wait(WaitPeriod.SHORT)
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsDescription))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsShowComments))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsCover))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsAuthors))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitleYear))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.books_retry_btn))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.books_progress_bar))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        coVerify(exactly = 1) {
            mockSearchForBooksService.getBookDetails(any())
            mockSearchForBooksService.getBookDetails(fakeResponseBookId)
        }
    }


    @Test
    fun testLoadingBookList_errorCase() {
        val fakeResponseBookId = 123L
        coEvery { mockSearchForBooksService.getBookDetails(fakeResponseBookId) }.coAnswers {
            throw RuntimeException("Fake exception!")
        }

        launchFragmentInContainer<BookDetailsFragment>(
            BookDetailsFragment.newInstance(fakeResponseBookId, "Fake title").arguments,
            themeResId = R.style.AppTheme
        )

        wait(WaitPeriod.SHORT)
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsDescription))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsShowComments))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsCover))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitle))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsAuthors))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitleYear))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.books_retry_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.books_progress_bar))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        coVerify(exactly = 1) {
            mockSearchForBooksService.getBookDetails(any())
            mockSearchForBooksService.getBookDetails(fakeResponseBookId)
        }
    }

    @Test
    fun testLoadingBookList_isProgressBarShownCase() {
        val fakeResponseBookId = 123L
        coEvery { mockSearchForBooksService.getBookDetails(fakeResponseBookId) }.coAnswers {
            withContext(Dispatchers.IO) {
                delay(WaitPeriod.LONG.timeInMillis)
                Response.success(mockSearchForBooksService.getBookDetailsSuccessfulResponseBody)
            }
        }

        launchFragmentInContainer<BookDetailsFragment>(
            BookDetailsFragment.newInstance(fakeResponseBookId, "Fake title").arguments,
            themeResId = R.style.AppTheme
        )

        wait(WaitPeriod.SHORT)
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsDescription))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsShowComments))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsCover))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitle))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsAuthors))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitleYear))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitleYear))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.books_retry_btn))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.books_progress_bar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testLoadingBookList_retryButton() {
        val fakeResponseBookId = 123L
        coEvery { mockSearchForBooksService.getBookDetails(fakeResponseBookId) }.coAnswers {
            throw RuntimeException("Fake exception!")
        }

        launchFragmentInContainer<BookDetailsFragment>(
            BookDetailsFragment.newInstance(fakeResponseBookId, "Fake title").arguments,
            themeResId = R.style.AppTheme
        )

        wait(WaitPeriod.SHORT)
        coVerify(exactly = 1) {
            mockSearchForBooksService.getBookDetails(any())
            mockSearchForBooksService.getBookDetails(fakeResponseBookId)
        }
        coEvery { mockSearchForBooksService.getBookDetails(fakeResponseBookId) }.returns(
            Response.success(mockSearchForBooksService.getBookDetailsSuccessfulResponseBody)
        )
        Espresso.onView(ViewMatchers.withId(R.id.books_retry_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())

        wait(WaitPeriod.SHORT)
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsDescription))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsShowComments))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsCover))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsAuthors))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.bookDetailsTitleYear))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.books_retry_btn))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.books_progress_bar))
            .check(ViewAssertions.matches(IsNot.not(ViewMatchers.isDisplayed())))
        coVerify(exactly = 2) {
            mockSearchForBooksService.getBookDetails(any())
            mockSearchForBooksService.getBookDetails(fakeResponseBookId)
        }
    }
}