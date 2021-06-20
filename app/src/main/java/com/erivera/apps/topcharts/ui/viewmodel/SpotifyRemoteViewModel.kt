package com.erivera.apps.topcharts.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.spotify.SpotifyRemoteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpotifyRemoteViewModel @Inject constructor(private val spotifyRemoteManager: SpotifyRemoteManager) :
    ViewModel() {

    fun connect() {
        spotifyRemoteManager.connect()
    }

    fun disconnect() {
        spotifyRemoteManager.disconnect()
    }
}