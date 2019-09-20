package com.github.michalchojnacki.findbook.ui.booklist

import androidx.annotation.ColorRes
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.domain.model.Book

class BookListItemViewModel(book: Book) {
    val imageUrl = book.imageUrl
    val author = book.author
    val title = book.title
    val hasPublicationYear = book.originalPublicationYear != null
    val publicationYear = book.originalPublicationYear?.toString()
    val averageRating = book.averageRating.toString()
    val ratingsCount = "(${book.ratingsCount})"
    @ColorRes
    val averageRatingTextColor = when {
        book.averageRating >= 4.5 -> R.color.book_list_item_rating_45_50
        book.averageRating >= 4.0 -> R.color.book_list_item_rating_40_45
        book.averageRating >= 3.5 -> R.color.book_list_item_rating_35_40
        book.averageRating >= 3.0 -> R.color.book_list_item_rating_30_35
        book.averageRating >= 2.5 -> R.color.book_list_item_rating_25_30
        book.averageRating >= 2.0 -> R.color.book_list_item_rating_20_25
        book.averageRating >= 1.5 -> R.color.book_list_item_rating_15_20
        book.averageRating >= 1.0 -> R.color.book_list_item_rating_10_20
        else -> R.color.book_list_item_rating_00_10
    }
}