package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result

interface SearchForBooksDataSource {
    suspend fun searchForBooksWithQuery(query: String) : Result<List<Book>>
}