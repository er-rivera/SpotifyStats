package com.erivera.apps.topcharts

import android.graphics.drawable.GradientDrawable

internal class CustomGradientDrawable(fromColor: Int, centerColor: Int, endColor: Int) :
    GradientDrawable(Orientation.TL_BR, intArrayOf(fromColor, centerColor, endColor)) {
    init {
        cornerRadius = 0f
        gradientType = LINEAR_GRADIENT
        //gradientRadius = 90f
    }
}