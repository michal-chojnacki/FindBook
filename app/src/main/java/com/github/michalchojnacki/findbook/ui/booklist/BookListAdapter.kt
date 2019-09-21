package com.github.michalchojnacki.findbook.ui.booklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.BookListItemBinding
import com.github.michalchojnacki.findbook.domain.model.Book

class BookListAdapter(private val requestManager: RequestManager, private val books: List<Book>) :
    RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    class ViewHolder(val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return ViewHolder(BookListItemBinding.bind(root))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.viewModel = BookListItemViewModel(requestManager, books[position])
    }

    override fun getItemCount() = books.size
}

@BindingAdapter("requestManager", "bookListAdapter", requireAll = true)
fun RecyclerView.bindBookListAdapter(requestManager: RequestManager, books: List<Book>) {
    adapter = BookListAdapter(requestManager, books)
}