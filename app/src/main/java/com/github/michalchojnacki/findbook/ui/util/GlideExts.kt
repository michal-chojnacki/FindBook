package com.github.michalchojnacki.findbook.ui.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.RequestManager

object GlideExts {
    @BindingAdapter("requestManager", "remoteSrc", requireAll = true)
    @JvmStatic
    fun ImageView.loadImage(requestManager: RequestManager, imageUrl: String?) {
        requestManager.load(imageUrl ?: "").into(this)
    }
}