package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import java.io.IOException

class SearchForForBooksRemoteDataSource(
    private val service: SearchForBooksService,
    private val booksMapper: BooksMapper
) : SearchForBooksDataSource {
    override suspend fun searchForBooksWithQuery(query: String): Result<List<Book>> =
            safeApiCall(
                    call = { requestSearchForBooksWithQuery(query) },
                    errorMessage = "Error searching for books with $query!"
            )

    private suspend fun requestSearchForBooksWithQuery(query: String): Result<List<Book>> {
        val response = retryIO { service.searchForBooksWithQuery(query) }
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.Success(booksMapper.map(body))
            }
        }
        return Result.Error(
                IOException(
                        "Connection problem!\nError code: ${response.code()},\nError message: ${response.message()}"
                )
        )
    }
}