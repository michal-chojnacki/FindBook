package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.data.model.mapper.BookDetailsMapper
import com.github.michalchojnacki.findbook.data.model.mapper.BooksMapper
import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.BookDetails
import com.github.michalchojnacki.findbook.domain.model.Result
import dagger.Reusable
import java.io.IOException
import javax.inject.Inject

@Reusable
class SearchForForBooksRemoteDataSource @Inject constructor(
    private val service: SearchForBooksService,
    private val booksMapper: BooksMapper,
    private val bookDetailsMapper: BookDetailsMapper
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

    override suspend fun saveBooks(query: String, books: List<Book>) {
        throw UnsupportedOperationException()
    }

    override suspend fun loadBookDetails(bookId: Long): Result<BookDetails> =
        safeApiCall(
            call = { requestLoadBookDetails(bookId) },
            errorMessage = "Error loading books detail with id: $bookId!"
        )

    private suspend fun requestLoadBookDetails(bookId: Long): Result<BookDetails> {
        val response = retryIO { service.getBookDetails(bookId) }
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.Success(bookDetailsMapper.map(body))
            }
        }
        return Result.Error(
            IOException(
                "Connection problem!\nError code: ${response.code()},\nError message: ${response.message()}"
            )
        )
    }
}