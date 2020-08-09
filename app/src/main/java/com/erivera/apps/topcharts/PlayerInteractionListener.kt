package com.erivera.apps.topcharts

import android.graphics.drawable.Drawable

interface PlayerInteractionListener {

    fun onNextClick()

    fun onPrevClick()

    fun onPlayPauseClick()

    fun onAlbumArtLoaded(drawable: Drawable)
}