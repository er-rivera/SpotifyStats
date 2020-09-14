package com.erivera.apps.topcharts

import com.spotify.protocol.types.Track

interface SpotifyRemoteManager {
    fun connect()

    fun disconnect()

    fun addListener(viewModelListener: ViewModelListener)

    fun removeListener(viewModelListener: ViewModelListener)

    fun next()

    fun previous()

    fun togglePlayPause()

    interface ViewModelListener {
        fun onCurrentTrackChanged(track: Track)

        fun onPauseStateChanged(isPaused: Boolean)
    }
}