package com.github.michalchojnacki.findbook.ui.booklist

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.michalchojnacki.findbook.domain.model.Book
import kotlinx.android.synthetic.main.book_list_item.view.*

class BookView(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    fun bind(book: Book) {
        bookListItemAuthor.text = book.author
        bookListItemTitle.text = book.title

    }
}