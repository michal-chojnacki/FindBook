package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.data.di.MockSearchForBooksService
import com.github.michalchojnacki.findbook.di.DaggerTestAppComponent
import com.github.michalchojnacki.findbook.domain.model.Result
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

private const val FAKE_QUERY = "fake_query"

class SearchForBooksRemoteDataSourceTest {
    @Inject
    lateinit var mockSearchForBooksService: MockSearchForBooksService
    @Inject
    lateinit var booksMapper: BooksMapper
    @Inject
    lateinit var dataSource: SearchForForBooksRemoteDataSource

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun `test successful scenario`() = runBlocking {
        val fakeBooksSearchRawModel = mockSearchForBooksService.successfulResponseBody
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
            .returns(Response.success(fakeBooksSearchRawModel))

        val result = dataSource.searchForBooksWithQuery(FAKE_QUERY)

        Assert.assertTrue(result == Result.Success(booksMapper.map(fakeBooksSearchRawModel)))
        coVerify(exactly = 1) { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
    }

    @Test
    fun `test unsuccessful scenario`() = runBlocking {
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
            .answers { throw IOException("Fake exception") }

        val result = dataSource.searchForBooksWithQuery(FAKE_QUERY)

        Assert.assertTrue(result is Result.Error)
        coVerify(exactly = 3) { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
    }

    @Test
    fun `test retry`() = runBlocking {
        var isFirstAttempt = true
        val fakeBooksSearchRawModel = mockSearchForBooksService.successfulResponseBody
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
            .answers {
                if (isFirstAttempt) {
                    isFirstAttempt = false
                    throw IOException("Fake exception")
                } else {
                    Response.success(fakeBooksSearchRawModel)
                }
            }

        val result = dataSource.searchForBooksWithQuery(FAKE_QUERY)

        Assert.assertTrue(result == Result.Success(booksMapper.map(fakeBooksSearchRawModel)))
        coVerify(exactly = 2) { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
    }
}