package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SearchForBooksWithQueryUseCase(
        private val searchForBooksDataSource: SearchForBooksDataSource,
        private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String): Result<List<Book>> = withContext(coroutineDispatcher) {
        searchForBooksDataSource.searchForBooksWithQuery(query)
    }
}