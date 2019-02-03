package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.domain.model.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

private const val FAKE_QUERY = "fake_query"

class SearchForBooksRemoteDataSourceTest {
    private val fakeApiResultsProducer = FakeApiResultsProducer()
    private val mockSearchForBooksService = mockk<SearchForBooksService>()
    private val booksMapper = BooksMapper()
    private val dataSource =
        SearchForForBooksRemoteDataSource(mockSearchForBooksService, booksMapper, Dispatchers.Default)

    @Test
    fun `test successful scenario`() = runBlocking {
        val fakeBooksSearchRawModel = fakeApiResultsProducer.produceBooksSearchRawModel()
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
            .returns(async { Response.success(fakeBooksSearchRawModel) })

        val result = dataSource.searchForBooksWithQuery(FAKE_QUERY)

        Assert.assertTrue(result == Result.Success(booksMapper.map(fakeBooksSearchRawModel)))
        coVerify(exactly = 1) { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
    }

    @Test
    fun `test unsuccessful scenario`() = runBlocking {
        coEvery { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
            .returns(async { Response.error<BooksSearchRawModel>(500, mockk()) })

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
                async {
                    if (isFirstAttempt) {
                        isFirstAttempt = false
                        Response.error<BooksSearchRawModel>(500, mockk())
                    } else {
                        Response.success(fakeBooksSearchRawModel)
                    }
                }
            }

        val result = dataSource.searchForBooksWithQuery(FAKE_QUERY)

        Assert.assertTrue(result == Result.Success(booksMapper.map(fakeBooksSearchRawModel)))
        coVerify(exactly = 2) { mockSearchForBooksService.searchForBooksWithQuery(FAKE_QUERY) }
    }
}