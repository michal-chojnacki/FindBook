package com.github.michalchojnacki.findbook.ui.booklist

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.michalchojnacki.findbook.domain.model.Book

class BookView(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    fun bind(book: Book) {
        (getChildAt(0) as TextView).text = book.title
    }
}