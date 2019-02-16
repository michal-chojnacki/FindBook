package com.github.michalchojnacki.findbook.ui.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.BookListFragmentBinding
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BookListFragment : BaseFragment() {
    companion object {
        private const val ARG_QUERY = "com.github.michalchojnacki.findbook.ui.booklistBookListFragment.ARG_QUERY"

        fun newInstance(query: String) =
                BookListFragment().apply { arguments = Bundle().apply { putString(ARG_QUERY, query) } }
    }

    private val viewModel: BookListViewModel by viewModel { parametersOf(arguments!!.getString(ARG_QUERY)) }

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
}
