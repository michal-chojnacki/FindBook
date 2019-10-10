package com.github.michalchojnacki.findbook.data.model.mapper

import com.github.michalchojnacki.findbook.data.model.BooksSearchRawModel
import com.github.michalchojnacki.findbook.domain.model.Book
import dagger.Reusable
import javax.inject.Inject

@Reusable
class BooksMapper @Inject constructor() {
    fun map(booksSearchRawModel: BooksSearchRawModel): List<Book> {
        return booksSearchRawModel.search?.results?.map { work ->
            Book(
                    id = work.bestBook?.id ?: -1L,
                    title = work.bestBook?.title ?: "",
                    author = work.bestBook?.author?.name ?: "",
                    imageUrl = work.bestBook?.imageUrl ?: "",
                    ratingsCount = work.ratingsCount,
                    textReviewsCount = work.textReviewsCount,
                    originalPublicationYear = work.originalPublicationYear,
                averageRating = work.averageRating ?: 0.0
            )
        }?.filter { it.id >= 0L && it.title.isNotBlank() && it.author.isNotBlank() } ?: emptyList()
    }
}