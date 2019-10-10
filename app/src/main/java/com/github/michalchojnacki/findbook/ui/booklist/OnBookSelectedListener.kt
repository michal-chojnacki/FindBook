package com.github.michalchojnacki.findbook.ui.booklist

import com.github.michalchojnacki.findbook.domain.model.Book

interface OnBookSelectedListener {
    fun onBookSelected(book: Book)
}