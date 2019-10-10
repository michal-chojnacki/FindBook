package com.github.michalchojnacki.findbook.ui.util

import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion

@BindingAdapter("textColorRes")
fun TextView.bindTextColorRes(@ColorRes colorRes: Int) {
    if(colorRes == 0) {
        return
    }
    setTextColor(context.getColor(colorRes))
}

@BindingAdapter("html")
fun WebView.bindIFrame(html : String?) {
    loadData(html ?: "", "text/html", null)
}

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean?) = if (visible == true) View.VISIBLE else View.GONE