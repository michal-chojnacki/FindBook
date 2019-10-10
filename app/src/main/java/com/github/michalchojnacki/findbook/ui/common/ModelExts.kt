package com.github.michalchojnacki.findbook.ui.common

import androidx.annotation.ColorRes
import com.github.michalchojnacki.findbook.R

object ModelExts {
    @get:ColorRes
    val Double.averageRatingTextColor : Int get() = when {
        this >= 4.5 -> R.color.book_list_item_rating_45_50
        this >= 4.0 -> R.color.book_list_item_rating_40_45
        this >= 3.5 -> R.color.book_list_item_rating_35_40
        this >= 3.0 -> R.color.book_list_item_rating_30_35
        this >= 2.5 -> R.color.book_list_item_rating_25_30
        this >= 2.0 -> R.color.book_list_item_rating_20_25
        this >= 1.5 -> R.color.book_list_item_rating_15_20
        this >= 1.0 -> R.color.book_list_item_rating_10_20
        else -> R.color.book_list_item_rating_00_10
    }
}