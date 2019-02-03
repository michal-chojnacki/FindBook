package com.github.michalchojnacki.findbook.data

import com.github.michalchojnacki.findbook.domain.model.Book

class BooksMapper {
    fun map(booksSearchRawModel: BooksSearchRawModel): List<Book> {
        return booksSearchRawModel.search?.results?.map { work ->
            Book(
                title = work.bestBook?.title ?: "",
                author = work.bestBook?.author?.name ?: "",
                imageUrl = work.bestBook?.imageUrl ?: "",
                ratingsCount = work.ratingsCount,
                textReviewsCount = work.textReviewsCount,
                originalPublicationYear = work.originalPublicationYear,
                averageRating= work.averageRating
                )
        }?.filter { it.title.isNotBlank() && it.author.isNotBlank() } ?: emptyList()
    }
}