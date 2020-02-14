package com.erivera.apps.topcharts.viewmodels

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter(value = ["imageUrl"], requireAll = false)
fun ImageView.setImageUrl(url: String?) {
    url?.let {
        Glide.with(context)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
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