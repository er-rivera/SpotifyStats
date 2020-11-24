package com.erivera.apps.topcharts

import android.graphics.drawable.Drawable
import com.erivera.apps.topcharts.models.domain.AudioItem

interface PlayerInteractionListener {

    fun onNextClick()

    fun onPrevClick()

    fun onPlayPauseClick()

    fun onAlbumArtLoaded(drawable: Drawable)

    fun onInfoMenuClick()

    fun onGridItemClick(audioItem: AudioItem)
}