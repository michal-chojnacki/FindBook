package com.github.michalchojnacki.findbook.util

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

object TextViewExts {
    @BindingAdapter("textRes")
    @JvmStatic
    fun TextView.setTextRes(@StringRes resId: Int?) {
        visibility = if (resId != null) {
            setText(resId)
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}