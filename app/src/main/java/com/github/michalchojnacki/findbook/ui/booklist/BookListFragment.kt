package com.github.michalchojnacki.findbook.ui.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.BookListFragmentBinding
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import com.github.michalchojnacki.findbook.ui.common.EventObserver
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.activityViewModel
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.viewModel
import com.github.michalchojnacki.findbook.ui.di.injector
import com.github.michalchojnacki.findbook.ui.navigation.MainNavigationViewModel
import com.github.michalchojnacki.findbook.util.exhaustive

class BookListFragment : BaseFragment() {
    companion object {
        private const val ARG_QUERY = "com.github.michalchojnacki.findbook.ui.booklist.BookListFragment.ARG_QUERY"

        fun newInstance(query: String) =
            BookListFragment().apply { arguments = Bundle().apply { putString(ARG_QUERY, query) } }
    }

    private val viewModel: BookListViewModel by viewModel {
        injector.bookListViewModelFactory.create(
            requestManager = Glide.with(this),
            query = queryArg
        )
    }
    private val navigationViewModel: MainNavigationViewModel by activityViewModel { injector.mainNavigationViewModel }

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

        viewModel.uiResultLiveData.observe(this, EventObserver {
            when(it) {
                is BookListViewModel.UiResult.ShowBookDetails -> {
                    navigationViewModel.showBookDetails(it.book)
                }
            }.exhaustive
        })
    }

    override val toolbarTitle: String?
        get() = getString(R.string.book_list_title, queryArg)

    override val backButtonVisible = true

    private val queryArg: String
        get() = arguments?.getString(ARG_QUERY) ?: ""
}
