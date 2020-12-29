package com.erivera.apps.topcharts.ui.listener

import android.graphics.drawable.Drawable
import com.erivera.apps.topcharts.models.domain.AudioItem

interface PlayerInteractionListener {

    fun onNextClick()

    fun onPrevClick()

    fun onPlayPauseClick()

    fun onAlbumArtLoaded(drawable: Drawable)

    fun onInfoMenuClick()

    fun onArrowDownClick()

    fun onGridItemClick(audioItem: AudioItem)
}