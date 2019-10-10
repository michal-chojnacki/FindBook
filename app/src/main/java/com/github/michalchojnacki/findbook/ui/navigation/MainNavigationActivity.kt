package com.github.michalchojnacki.findbook.ui.navigation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.ui.bookdetails.BookDetailsFragment
import com.github.michalchojnacki.findbook.ui.bookdetails.BookDetailsReviewsFragment
import com.github.michalchojnacki.findbook.ui.booklist.BookListFragment
import com.github.michalchojnacki.findbook.ui.camera.OcrCaptureFragment
import com.github.michalchojnacki.findbook.ui.common.EventObserver
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.viewModel
import com.github.michalchojnacki.findbook.ui.di.injector
import com.github.michalchojnacki.findbook.ui.typingsearch.TypingSearchFragment
import com.github.michalchojnacki.findbook.util.exhaustive

class MainNavigationActivity : AppCompatActivity() {

    private val viewModel: MainNavigationViewModel by viewModel { injector.mainNavigationViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_navigation_activity)
        viewModel.uiResultLiveData.observe(this, EventObserver {
            when (it) {
                is MainNavigationViewModel.UiResult.ShowOcrScanner -> {
                    supportFragmentManager.commit {
                        replace(R.id.container, OcrCaptureFragment.newInstance())
                    }
                }
                is MainNavigationViewModel.UiResult.ShowBookList -> {
                    supportFragmentManager.commit {
                        addToBackStack(null)
                        replace(R.id.container, BookListFragment.newInstance(it.query))
                    }
                }
                is MainNavigationViewModel.UiResult.ShowTypingSearch -> {
                    supportFragmentManager.commit {
                        replace(R.id.container, TypingSearchFragment.newInstance())
                    }
                }
                is MainNavigationViewModel.UiResult.ShowBookDetails -> {
                    supportFragmentManager.commit {
                        addToBackStack(null)
                        replace(R.id.container, BookDetailsFragment.newInstance(it.book.id, it.book.title))
                    }
                }
                is MainNavigationViewModel.UiResult.ShowReviews -> {
                    supportFragmentManager.commit {
                        addToBackStack(null)
                        replace(R.id.container, BookDetailsReviewsFragment.newInstance(it.bookDetails.reviewsWidget, it.bookDetails.title))
                    }
                }
            }.exhaustive
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
