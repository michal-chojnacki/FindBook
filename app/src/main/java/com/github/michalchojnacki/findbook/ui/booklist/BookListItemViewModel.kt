package com.github.michalchojnacki.findbook.ui.booklist

import com.github.michalchojnacki.findbook.domain.model.Book

class BookListItemViewModel(private val book: Book) {
    val imageUrl = book.imageUrl
    val author = book.author
    val title = book.title
    val hasPublicationYear = book.originalPublicationYear != null
    val publicationYear = book.originalPublicationYear?.toString()
    val averageRating = book.averageRating.toString()
    val ratingsCount = "(${book.ratingsCount})"
}