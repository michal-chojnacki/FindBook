package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.BookDetails
import com.github.michalchojnacki.findbook.domain.model.Result
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchForForBooksLocalDataSource @Inject constructor() : SearchForBooksDataSource {
     private val cache = mutableMapOf<String, List<Book>>()

    override suspend fun saveBooks(query: String, books: List<Book>) {
        cache[query] = books
    }

    override suspend fun searchForBooksWithQuery(query: String): Result<List<Book>> {
        return cache[query]?.let { Result.Success(it) }
            ?: Result.Error(IOException("Nothing was cached!"))
    }

    override suspend fun loadBookDetails(bookId: Long): Result<BookDetails> {
        return Result.Error(UnsupportedOperationException("Method not available for local data source!"))
    }
}