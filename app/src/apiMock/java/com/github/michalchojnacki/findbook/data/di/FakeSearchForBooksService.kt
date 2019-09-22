package com.github.michalchojnacki.findbook.data.di

import com.github.michalchojnacki.findbook.data.BooksSearchRawModel
import com.github.michalchojnacki.findbook.data.SearchForBooksService
import org.simpleframework.xml.core.Persister
import retrofit2.Response
import javax.inject.Inject

class FakeSearchForBooksService @Inject constructor() : SearchForBooksService {
    private val serializer = Persister()
    var currentResponse: Response<BooksSearchRawModel> =
        Response.success(getResponse(FakeResponse.BOOKS_SUCCESSFUL_RESPONSE.path))

    override suspend fun searchForBooksWithQuery(query: String): Response<BooksSearchRawModel> {
        return currentResponse
    }

    private fun getResponse(path: String): BooksSearchRawModel {
        return serializer.read(
            BooksSearchRawModel::class.java,
            this.javaClass.classLoader.getResourceAsStream(path)
        )
    }

    enum class FakeResponse(val path: String) {
        BOOKS_SUCCESSFUL_RESPONSE("fakeApi/books_successful_response.xml")
    }
}