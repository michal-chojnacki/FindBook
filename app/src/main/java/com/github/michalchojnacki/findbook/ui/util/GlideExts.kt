package com.github.michalchojnacki.findbook.ui.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.michalchojnacki.findbook.domain.model.Result
import java.io.IOException

object GlideExts {
    @BindingAdapter("requestManager", "remoteSrc", "onResult", requireAll = true)
    @JvmStatic
    fun ImageView.loadImage(requestManager: RequestManager, imageUrl: String?, onResult: ((Result<Unit>) -> Unit)?) {
        requestManager.load(imageUrl ?: "").addListener(object: RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onResult?.invoke(Result.Error(e ?: IOException("Unknown exception")))
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onResult?.invoke(Result.Success(Unit))
                return false
            }

        }).into(this)
    }
}