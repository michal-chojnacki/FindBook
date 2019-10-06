package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.BookDetails
import com.github.michalchojnacki.findbook.domain.model.Result

interface SearchForBooksDataSource {
    suspend fun searchForBooksWithQuery(query: String): Result<List<Book>>

    suspend fun saveBooks(query: String, books: List<Book>)

    suspend fun loadBookDetails(bookId : Long) : Result<BookDetails>
}