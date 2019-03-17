package com.github.michalchojnacki.findbook.ui.util

import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion

@BindingAdapter("textColorRes")
fun TextView.bindTextColorRes(@ColorRes colorRes: Int) {
    setTextColor(context.getColor(colorRes))
}

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean?) = if (visible == true) View.VISIBLE else View.GONE