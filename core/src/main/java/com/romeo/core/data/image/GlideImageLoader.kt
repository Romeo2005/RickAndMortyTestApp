package com.romeo.core.data.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GlideImageLoader : ImageLoader<ImageView> {
    override fun loadImage(
        target: ImageView,
        url: String,
        listener: ImageLoadingListener?,
        loadingCode: Int?,
    ) {
        Glide.with(target)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ) = listener?.onLoadingError(loadingCode, e) ?: false

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ) = listener?.onLoadingSuccess(loadingCode) ?: false
            })
            .into(target)
    }

    override fun loadImage(
        target: ImageView,
        url: String,
        imageInsertScope: CoroutineScope
    ) {
        val drawable = Glide.with(target.context)
            .load(url)
            .submit(500, 500).get()

        imageInsertScope.launch {
            target.setImageDrawable(drawable)
        }
    }

}