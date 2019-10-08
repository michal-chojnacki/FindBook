package com.github.michalchojnacki.findbook.data.di

import com.github.michalchojnacki.findbook.data.SearchForBooksService
import com.github.michalchojnacki.findbook.data.model.BookDetailsRawModel
import com.github.michalchojnacki.findbook.data.model.BooksSearchRawModel
import io.mockk.coEvery
import io.mockk.mockk
import org.simpleframework.xml.core.Persister
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockSearchForBooksService @Inject constructor() : SearchForBooksService by mockk() {
    private val serializer = Persister()
    val searchForBooksWithQuerySuccessfulResponseBody: BooksSearchRawModel get() = getResponseBody(FakeResponse.BOOKS_SUCCESSFUL_RESPONSE)
    val searchForBooksWithQueryEmptyResponseBody: BooksSearchRawModel get() = BooksSearchRawModel()
    val getBookDetailsSuccessfulResponseBody: BookDetailsRawModel get() = getResponseBody(FakeResponse.BOOK_DETAILS_SUCCESSFUL_RESPONSE)

    init {
        coEvery { searchForBooksWithQuery(any()) }.returns(Response.success(searchForBooksWithQuerySuccessfulResponseBody))
        coEvery { getBookDetails(any()) }.returns(Response.success(getBookDetailsSuccessfulResponseBody))
    }

    private inline fun <reified T> getResponseBody(fakeResponse: FakeResponse): T {
        return serializer.read(
            T::class.java,
            this.javaClass.classLoader.getResourceAsStream(fakeResponse.path)
        )
    }


    private enum class FakeResponse(val path: String) {
        BOOKS_SUCCESSFUL_RESPONSE("fakeApi/books_successful_response.xml"),
        BOOK_DETAILS_SUCCESSFUL_RESPONSE("fakeApi/book_details_successful_response.xml")
    }
}