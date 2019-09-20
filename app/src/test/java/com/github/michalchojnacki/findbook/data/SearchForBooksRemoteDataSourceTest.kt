package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.domain.model.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

private const val FAKE_QUERY = "fake_query"

class SearchForBooksRemoteDataSourceTest {
    private val fakeApiResultsProducer = FakeApiResultsProducer()
    private val mockSearchForBooksService = mockk<SearchForBooksService>()
    private val booksMapper = BooksMapper()
    private val dataSource =
        SearchForForBooksRemoteDataSource(mockSearchForBooksService, booksMapper)

    @Test
    fun `test successful scenario`() = runBlocking {
        val fakeBooksSearchRawModel = fakeApiResultsProducer.produceBooksSearchRawModel()
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
        val fakeBooksSearchRawModel = fakeApiResultsProducer.produceBooksSearchRawModel()
        var isFirstAttempt = true
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