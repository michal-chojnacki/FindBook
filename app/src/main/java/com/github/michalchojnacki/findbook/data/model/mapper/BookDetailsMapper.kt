package com.github.michalchojnacki.findbook.data.model.mapper

import com.github.michalchojnacki.findbook.data.model.BookDetailsRawModel
import com.github.michalchojnacki.findbook.domain.model.BookDetails
import dagger.Reusable
import javax.inject.Inject

@Reusable
class BookDetailsMapper @Inject constructor() {
    fun map(booksSearchRawModel: BookDetailsRawModel): BookDetails {
        return BookDetails(id = booksSearchRawModel.book?.id ?: -1L,
            title = booksSearchRawModel.book?.title ?: "",
            authors = booksSearchRawModel.book?.authors?.mapNotNull { it.name } ?: emptyList(),
            imageUrl = booksSearchRawModel.book?.imageUrl ?: "",
            ratingsCount = booksSearchRawModel.book?.work?.ratingsCount ?: 0,
            textReviewsCount = booksSearchRawModel.book?.work?.textReviewsCount ?: 0,
            originalPublicationYear = booksSearchRawModel.book?.work?.originalPublicationYear ?: 0,
            averageRating = booksSearchRawModel.book?.averageRating ?: 0.0,
            reviewsWidget = booksSearchRawModel.book?.reviewsWidget ?: "",
            description = booksSearchRawModel.book?.description ?: "")
    }
}
