package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.domain.model.Book
import dagger.Reusable
import javax.inject.Inject

@Reusable
class BooksMapper @Inject constructor() {
    fun map(booksSearchRawModel: BooksSearchRawModel): List<Book> {
        return booksSearchRawModel.search?.results?.map { work ->
            Book(
                    title = work.bestBook?.title ?: "",
                    author = work.bestBook?.author?.name ?: "",
                    imageUrl = work.bestBook?.imageUrl ?: "",
                    ratingsCount = work.ratingsCount,
                    textReviewsCount = work.textReviewsCount,
                    originalPublicationYear = work.originalPublicationYear,
                averageRating = work.averageRating ?: 0.0
            )
        }?.filter { it.title.isNotBlank() && it.author.isNotBlank() } ?: emptyList()
    }
}