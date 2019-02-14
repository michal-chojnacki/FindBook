package com.github.michalchojnacki.findbook.ui.booklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.domain.model.Book

class BookListAdapter(private val books: List<Book>) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    class ViewHolder(root: View) : RecyclerView.ViewHolder(root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView as BookView).bind(books[position])
    }

    override fun getItemCount() = books.size
}

@BindingAdapter("bookListAdapter")
fun RecyclerView.bindBookListAdapter(books: List<Book>) {
    adapter = BookListAdapter(books)
}
