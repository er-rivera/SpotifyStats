package com.erivera.apps.topcharts

import android.view.View

fun View.addStatusBarTopPadding() {
    this.setOnApplyWindowInsetsListener { v, insets ->
        val newPadding = paddingTop + insets.systemWindowInsetTop // status bar height
        v.setPadding(paddingLeft, newPadding, paddingRight, paddingBottom)
        insets
    }
}