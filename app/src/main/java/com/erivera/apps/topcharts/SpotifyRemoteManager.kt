package com.erivera.apps.topcharts

import com.spotify.protocol.types.PlayerState

interface SpotifyRemoteManager {
    fun connect()

    fun disconnect()

    fun addListener(viewModelListener: ViewModelListener)

    fun removeListener(viewModelListener: ViewModelListener)

    fun next()

    fun previous()

    fun resume()

    fun pause()

    fun isPaused(): Boolean?

    interface ViewModelListener {
        fun onConnected()
        fun onFailure(error: Throwable)
        fun onNextPlayerState(playerState: PlayerState)
        fun onNextPlayerError(error: Throwable)
    }
}