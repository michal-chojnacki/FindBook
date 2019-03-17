package com.github.michalchojnacki.findbook.ui.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("remoteSrc")
fun ImageView.loadImage(imageUrl: String) {
    Glide.with(context).load(imageUrl).into(this)
}