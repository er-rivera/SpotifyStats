package com.erivera.apps.topcharts.viewmodels

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.erivera.apps.topcharts.PlayerInteractionListener

@BindingAdapter(value = ["imageUrl", "viewListener"], requireAll = false)
fun ImageView.setImageUrl(url: String? = null, listener: PlayerInteractionListener? = null) {
    url?.let {
        Glide.with(context)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(Glide::class.java.name, "Error Loading Image")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    resource?.let { drawable ->
                        listener?.onAlbumArtLoaded(drawable)
                    }
                    return false
                }
            })
            .into(this)
    }
}

@BindingAdapter(value = ["updateText"], requireAll = false)
fun TextView.updateText(text: String?) {
    text?.let {
        this.text = it
        this.ellipsize = TextUtils.TruncateAt.MARQUEE
        this.setSingleLine(true)
        this.marqueeRepeatLimit = 5
        this.isSelected = true
    }
}