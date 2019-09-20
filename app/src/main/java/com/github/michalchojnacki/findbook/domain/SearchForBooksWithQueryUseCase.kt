package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.domain.model.Result
import dagger.Reusable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
class SearchForBooksWithQueryUseCase @Inject constructor(
    private val searchForBooksDataSource: SearchForBooksDataSource
) {
    suspend operator fun invoke(query: String): Result<List<Book>> =
        withContext(Dispatchers.Default) {
        searchForBooksDataSource.searchForBooksWithQuery(query)
    }
}