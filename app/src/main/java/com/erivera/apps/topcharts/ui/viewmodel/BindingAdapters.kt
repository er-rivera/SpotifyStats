package com.erivera.apps.topcharts.ui.viewmodel

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.erivera.apps.topcharts.ui.listener.PlayerInteractionListener
import com.erivera.apps.topcharts.R
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.glide.transformations.BlurTransformation


@BindingAdapter(value = ["imageUrl", "viewListener", "blur"], requireAll = false)
fun ImageView.setImageUrl(
    url: String? = null,
    listener: PlayerInteractionListener? = null,
    blur: Boolean? = null
) {
    url?.let {
        val factory =
            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        Glide.with(context)
            .load(it)
            .transition(withCrossFade(factory))
            .placeholder(ColorDrawable(Color.BLACK))
            .diskCacheStrategy(DiskCacheStrategy.ALL).apply {
                if (blur == true) {
                    this.apply(bitmapTransform(BlurTransformation(25, 3)))
                }
            }.listener(object : RequestListener<Drawable> {
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
        this.isSingleLine = true
        this.marqueeRepeatLimit = 5
        this.isSelected = true
    }
}

@BindingAdapter(value = ["glowColor"], requireAll = false)
fun ImageView.setGlowColor(colorArray: Array<Int>?) {
    val color = colorArray?.getOrNull(0) ?: colorArray?.getOrNull(1) ?: colorArray?.getOrNull(2)
    ?: ContextCompat.getColor(context, R.color.white)
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

@BindingAdapter(value = ["ffGlowColor"], requireAll = false)
fun ImageView.setFFGlowColor(colorArray: Array<Int>?) {
    val color = colorArray?.getOrNull(0) ?: colorArray?.getOrNull(1) ?: colorArray?.getOrNull(2)
    ?: ContextCompat.getColor(context, R.color.white)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        (this.drawable as? StateListDrawable)?.getStateDrawable(0)?.let {
            DrawableCompat.setTint(it, color)
        }
        (this.drawable as? StateListDrawable)?.getStateDrawable(1)?.let {
            DrawableCompat.setTint(it, color)
        }
    }
}

@BindingAdapter(value = ["bar_layout_height"], requireAll = false)
fun AppBarLayout.setLayoutHeight(value: Int?) {
    value?.let {
        (this.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
            height = it
            this@setLayoutHeight.layoutParams = this
        }
    }
}

@BindingAdapter(value = ["minHeight"], requireAll = false)
fun View.setMinHeight(value: Int?) {
    value?.let {
        (this.layoutParams as? AppBarLayout.LayoutParams)?.apply {
            minimumHeight = (it)
            this@setMinHeight.layoutParams = this
        }
    }
}

@BindingAdapter(value = ["drawableRes"], requireAll = false)
fun ImageView.setDrawableRes(value: Int?) {
    value?.let {
        this.setImageDrawable(ContextCompat.getDrawable(context, value))
    } ?: let {
        visibility = View.GONE
    }
}