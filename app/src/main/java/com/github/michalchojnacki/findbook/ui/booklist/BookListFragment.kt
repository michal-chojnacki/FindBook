package com.github.michalchojnacki.findbook.ui.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.BookListFragmentBinding
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.viewModel
import com.github.michalchojnacki.findbook.ui.di.injector

class BookListFragment : BaseFragment() {
    companion object {
        private const val ARG_QUERY = "com.github.michalchojnacki.findbook.ui.booklistBookListFragment.ARG_QUERY"

        fun newInstance(query: String) =
            BookListFragment().apply { arguments = Bundle().apply { putString(ARG_QUERY, query) } }
    }

    private val viewModel: BookListViewModel by viewModel {
        injector.bookListViewModelFactory.create(
            query = queryArg
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.book_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BookListFragmentBinding.bind(view).apply {
            lifecycleOwner = this@BookListFragment
        }.viewModel = viewModel
    }

    override val toolbarTitle: String?
        get() = getString(R.string.book_list_title, queryArg)

    override val backButtonVisible = true

    private val queryArg: String
        get() = arguments?.getString(ARG_QUERY) ?: ""
}
