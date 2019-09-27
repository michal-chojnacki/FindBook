package com.github.michalchojnacki.findbook.ui.booklist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.data.BooksSearchRawModel
import com.github.michalchojnacki.findbook.data.di.MockSearchForBooksService
import com.github.michalchojnacki.findbook.ui.di.InjectorProvider
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BookListFragmentTest {

    private val mockSearchForBooksService =
        (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
                as InjectorProvider).component.searchForBooksService as MockSearchForBooksService

    @Before
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun testLoadingBookList_happyCaseScenario() {
        val fakeQuery = "test query"
        val fakeResponse = mockSearchForBooksService.successfulResponseBody
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(fakeQuery) }.returns(
            Response.success(fakeResponse)
        )

        launchFragmentInContainer<BookListFragment>(
            BookListFragment.newInstance(fakeQuery).arguments,
            themeResId = R.style.AppTheme
        )

        runBlocking { delay(500) }

        coVerify(exactly = 1) {
            mockSearchForBooksService.searchForBooksWithQuery(any())
            mockSearchForBooksService.searchForBooksWithQuery(fakeQuery)
        }
        onView(withId(R.id.books_error_tv)).check(matches(not(isDisplayed())))
        onView(withId(R.id.books_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.books_rv)).check(matches(isDisplayed()))
        onView(withText(fakeResponse.search!!.results!![0].bestBook!!.title)).check(
            matches(isDisplayed())
        )
        onView(withText(fakeResponse.search!!.results!![1].bestBook!!.title)).check(
            matches(isDisplayed())
        )
        onView(withText(fakeResponse.search!!.results!![2].bestBook!!.title)).check(
            matches(isDisplayed())
        )
        onView(withText(fakeResponse.search!!.results!![3].bestBook!!.title)).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun testLoadingBookList_errorCase() {
        val fakeQuery = "test query with error"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(fakeQuery) }.answers {
            throw RuntimeException("Fake exception!")
        }

        launchFragmentInContainer<BookListFragment>(
            BookListFragment.newInstance(fakeQuery).arguments,
            themeResId = R.style.AppTheme
        )

        runBlocking { delay(500) }
        coVerify(exactly = 1) {
            mockSearchForBooksService.searchForBooksWithQuery(any())
            mockSearchForBooksService.searchForBooksWithQuery(fakeQuery)
        }
        onView(withId(R.id.books_error_tv)).check(matches(isDisplayed())).check(matches(withText(R.string.book_list_error)))
        onView(withId(R.id.books_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.books_rv)).check(matches(isDisplayed()))
    }


    @Test
    fun testLoadingBookList_isProgressBarShownCase() {
        val fakeQuery = "test query 3"
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(fakeQuery) }.answers {
            runBlocking { delay(500) }
            Response.success(BooksSearchRawModel())
        }

        launchFragmentInContainer<BookListFragment>(
            BookListFragment.newInstance(fakeQuery).arguments,
            themeResId = R.style.AppTheme
        )

        onView(withId(R.id.books_error_tv)).check(matches(not(isDisplayed())))
        onView(withId(R.id.books_progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.books_rv)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoadingBookList_emptyResponse() {
        val fakeQuery = "test query with empty response"
        val fakeResponse = mockSearchForBooksService.emptyResponseBody
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(fakeQuery) }.returns(Response.success(fakeResponse))

        launchFragmentInContainer<BookListFragment>(
            BookListFragment.newInstance(fakeQuery).arguments,
            themeResId = R.style.AppTheme
        )

        runBlocking { delay(500) }

        coVerify(exactly = 1) {
            mockSearchForBooksService.searchForBooksWithQuery(any())
            mockSearchForBooksService.searchForBooksWithQuery(fakeQuery)
        }
        onView(withId(R.id.books_error_tv)).check(matches(isDisplayed())).check(matches(withText(R.string.book_list_empty_error)))
        onView(withId(R.id.books_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.books_rv)).check(matches(isDisplayed()))
    }
}