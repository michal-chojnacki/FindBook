package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.data.di.Local
import com.github.michalchojnacki.findbook.data.di.Remote
import com.github.michalchojnacki.findbook.domain.SearchForBooksDataSource
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.BookDetails
import com.github.michalchojnacki.findbook.domain.model.Result
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SearchForBooksRepository @Inject constructor(
    @Remote private val searchForForBooksRemoteDataSource: SearchForBooksDataSource,
    @Local private val searchForForBooksLocalDataSource: SearchForBooksDataSource
) : SearchForBooksDataSource {
       override suspend fun saveBooks(query: String, books: List<Book>) {
        return searchForForBooksLocalDataSource.saveBooks(query, books)
    }

    override suspend fun searchForBooksWithQuery(query: String): Result<List<Book>> {
        (searchForForBooksLocalDataSource.searchForBooksWithQuery(query)).takeIf { it is Result.Success }
            ?.let { return it }
        return searchForForBooksRemoteDataSource.searchForBooksWithQuery(query).also {
            it.let { return@let (it as? Result.Success)?.data }
                ?.let { books -> searchForForBooksLocalDataSource.saveBooks(query, books) }
        }
    }

    override suspend fun loadBookDetails(bookId: Long): Result<BookDetails> {
        return searchForForBooksRemoteDataSource.loadBookDetails(bookId)
    }
}