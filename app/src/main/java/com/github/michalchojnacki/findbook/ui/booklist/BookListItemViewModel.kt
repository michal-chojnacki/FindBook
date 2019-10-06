package com.github.michalchojnacki.findbook.ui.booklist

import androidx.annotation.ColorRes
import com.bumptech.glide.RequestManager
import com.github.michalchojnacki.findbook.domain.model.Book
import com.github.michalchojnacki.findbook.ui.common.ModelExts.averageRatingTextColor

class BookListItemViewModel(val requestManager: RequestManager,
                            book: Book,
                            private val onItemClicked : () -> Unit) {
    val imageUrl = book.imageUrl
    val author = book.author
    val title = book.title
    val hasPublicationYear = book.originalPublicationYear != null
    val publicationYear = book.originalPublicationYear?.toString()
    val averageRating = book.averageRating.toString()
    @ColorRes
    val averageRatingTextColor = book.averageRating.averageRatingTextColor
    val ratingsCount = "(${book.ratingsCount})"

    fun onClicked() {
        onItemClicked()
    }
}