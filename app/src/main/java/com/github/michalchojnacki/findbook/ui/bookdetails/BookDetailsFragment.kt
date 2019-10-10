package com.github.michalchojnacki.findbook.ui.bookdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.BookDetailsFragmentBinding
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import com.github.michalchojnacki.findbook.ui.common.EventObserver
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.activityViewModel
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.viewModel
import com.github.michalchojnacki.findbook.ui.di.injector
import com.github.michalchojnacki.findbook.ui.navigation.MainNavigationViewModel
import com.github.michalchojnacki.findbook.util.exhaustive

class BookDetailsFragment : BaseFragment() {
    companion object {
        private const val ARG_BOOK_ID =
            "com.github.michalchojnacki.findbook.ui.bookdetails.BookDetailsFragment.ARG_BOOK_ID"
        private const val ARG_TITLE =
            "com.github.michalchojnacki.findbook.ui.bookdetails.BookDetailsFragment.ARG_TITLE"

        fun newInstance(bookId: Long, title: String) =
            BookDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_BOOK_ID, bookId)
                    putString(ARG_TITLE, title)
                }
            }
    }

    private val viewModel: BookDetailsViewModel by viewModel {
        injector.bookDetailsViewModelFactory.create(
            requestManager = Glide.with(this),
            bookId = bookIdArg
        )
    }
    private val navigationViewModel: MainNavigationViewModel by activityViewModel { injector.mainNavigationViewModel }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.book_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BookDetailsFragmentBinding.bind(view).apply {
            lifecycleOwner = this@BookDetailsFragment
        }.viewModel = viewModel
        viewModel.uiResultLiveData.observe(this, EventObserver {
            when(it) {
                is BookDetailsViewModel.UiResult.ShowReviews -> navigationViewModel.showReviews(it.bookDetails)
            }.exhaustive
        })
    }

    override val toolbarTitle: String?
        get() = titleArg

    override val backButtonVisible = true

    private val bookIdArg: Long
        get() = arguments?.getLong(ARG_BOOK_ID)!!

    private val titleArg: String
        get() = arguments?.getString(ARG_TITLE) ?: ""
}