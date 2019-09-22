package com.github.michalchojnacki.findbook.data.di

import com.github.michalchojnacki.findbook.data.BooksSearchRawModel
import com.github.michalchojnacki.findbook.data.SearchForBooksService
import io.mockk.coEvery
import io.mockk.mockk
import org.simpleframework.xml.core.Persister
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockSearchForBooksService @Inject constructor() : SearchForBooksService by mockk() {
    private val serializer = Persister()
    val successfulResponseBody: BooksSearchRawModel get() = getResponseBody(FakeResponse.BOOKS_SUCCESSFUL_RESPONSE)
    val emptyResponseBody: BooksSearchRawModel get() = BooksSearchRawModel()

    init {
        coEvery { searchForBooksWithQuery(any()) }.returns(Response.success(successfulResponseBody))
    }

    private fun getResponseBody(fakeResponse: FakeResponse): BooksSearchRawModel {
        return serializer.read(
            BooksSearchRawModel::class.java,
            this.javaClass.classLoader.getResourceAsStream(fakeResponse.path)
        )
    }


    private enum class FakeResponse(val path: String) {
        BOOKS_SUCCESSFUL_RESPONSE("fakeApi/books_successful_response.xml")
    }
}