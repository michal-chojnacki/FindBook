package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.di.DaggerTestAppComponent
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

private const val FAKE_QUERY = "fake_query"

class SearchForBooksWithQueryUseCaseTest {
    private val searchForBooksDataSource: SearchForBooksDataSource = mockk()
    @Inject
    lateinit var searchForBooksWithQuery: SearchForBooksWithQueryUseCase

    @Before
    fun setUp() {
        DaggerTestAppComponent.factory().create(searchForBooksDataSource).inject(this)
    }

    @Test
    fun `verify invoking searching on data source`() = runBlocking {
        val mockBookList = listOf<Book>(mockk(), mockk())
        coEvery { searchForBooksDataSource.searchForBooksWithQuery(FAKE_QUERY) }.returns(
            Result.Success(
                mockBookList
            )
        )

        val result = searchForBooksWithQuery(FAKE_QUERY)

        coVerify(exactly = 1) { searchForBooksDataSource.searchForBooksWithQuery(FAKE_QUERY) }
        Assert.assertEquals(Result.Success(mockBookList), result)
    }

    @Test
    fun `verify invoking searching on data source with failed scenario`() = runBlocking {
        val fakeException = IOException("Fake exception!")
        coEvery { searchForBooksDataSource.searchForBooksWithQuery(FAKE_QUERY) }.returns(
            Result.Error(
                fakeException
            )
        )

        val result = searchForBooksWithQuery(FAKE_QUERY)

        coVerify(exactly = 1) { searchForBooksDataSource.searchForBooksWithQuery(FAKE_QUERY) }
        Assert.assertEquals(Result.Error(fakeException), result)
    }
}