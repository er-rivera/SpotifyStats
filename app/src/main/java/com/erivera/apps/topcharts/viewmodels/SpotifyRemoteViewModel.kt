package com.erivera.apps.topcharts.viewmodels

import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.SpotifyRemoteManager
import javax.inject.Inject

class SpotifyRemoteViewModel @Inject constructor(private val spotifyRemoteManager: SpotifyRemoteManager) :
    ViewModel() {

    fun connect() {
        spotifyRemoteManager.connect()
    }

    fun disconnect() {
        spotifyRemoteManager.disconnect()
    }
}