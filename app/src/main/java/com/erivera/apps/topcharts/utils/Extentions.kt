package com.erivera.apps.topcharts.utils

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes

fun View.addStatusBarTopPadding() {
    this.setOnApplyWindowInsetsListener { v, insets ->
        val newPadding = paddingTop + insets.systemWindowInsetTop // status bar height
        v.setPadding(paddingLeft, newPadding, paddingRight, paddingBottom)
        insets
    }
}

fun Resources.getDimenPercentage(@DimenRes res: Int) : Float {
    val outValue = TypedValue()
    this.getValue(
        res,
        outValue,
        true
    )
    return outValue.float
}