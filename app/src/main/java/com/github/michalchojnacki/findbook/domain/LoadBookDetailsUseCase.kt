package com.github.michalchojnacki.findbook.domain

import com.github.michalchojnacki.findbook.domain.model.BookDetails
import com.github.michalchojnacki.findbook.domain.model.Result
import dagger.Reusable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
class LoadBookDetailsUseCase  @Inject constructor(
    private val searchForBooksDataSource: SearchForBooksDataSource
) {
    suspend operator fun invoke(bookId: Long): Result<BookDetails> =
        withContext(Dispatchers.Default) {
            searchForBooksDataSource.loadBookDetails(bookId)
        }
}