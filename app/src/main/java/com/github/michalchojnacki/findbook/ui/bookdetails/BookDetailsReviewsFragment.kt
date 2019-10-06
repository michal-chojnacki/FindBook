package com.github.michalchojnacki.findbook.ui.bookdetails

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.databinding.BookDetailsReviewsBinding
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import kotlin.math.roundToInt

class BookDetailsReviewsFragment : BaseFragment() {
    companion object {
        private const val ARG_REVIEWS_WIDGET =
            "com.github.michalchojnacki.findbook.ui.bookdetails.BookDetailsReviewsFragment.ARG_REVIEWS_WIDGET"
        private const val ARG_TITLE =
            "com.github.michalchojnacki.findbook.ui.bookdetails.BookDetailsReviewsFragment.ARG_TITLE"

        fun newInstance(reviewsWidget: String, title: String) =
            BookDetailsReviewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_REVIEWS_WIDGET, reviewsWidget)
                    putString(ARG_TITLE, title)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.book_details_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            BookDetailsReviewsBinding.bind(view).apply {
                lifecycleOwner = this@BookDetailsReviewsFragment
            }.reviewsWidget = adjustSize(view, reviewsWidgetArg)
        }
    }

    override val toolbarTitle: String?
        get() = getString(R.string.book_details_reviews_title, titleArg)

    override val backButtonVisible = true

    private fun adjustSize(view: View, reviewsWidget: String): String {
        val newWidth = view.measuredWidth.pxToDp - resources.getDimension(R.dimen.margin_sm)
        val newHeight = view.measuredHeight.pxToDp
        return reviewsWidget
            .replace(Regex("height=\"\\d*\""), "height=\"${newHeight}\"")
            .replace(Regex("width=\"\\d*\""), "width=\"${newWidth}\"")
            .replace(Regex("width:\\d*px"), "width:${newWidth}px")
    }

    private val Int.pxToDp: Int get() {
        val context = context ?: return 0
        val displayMetrics = context.resources.displayMetrics
        return (this / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    private val reviewsWidgetArg: String
        get() = arguments?.getString(ARG_REVIEWS_WIDGET) ?: ""

    private val titleArg: String
        get() = arguments?.getString(ARG_TITLE) ?: ""
}