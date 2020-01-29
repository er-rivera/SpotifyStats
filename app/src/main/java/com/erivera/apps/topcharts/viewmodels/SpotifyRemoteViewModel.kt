package com.erivera.apps.topcharts.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.SpotifyRemoteManager
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import javax.inject.Inject

class SpotifyRemoteViewModel @Inject constructor(private val spotifyRemoteManager: SpotifyRemoteManager) :
    ViewModel() {

    private val _currentTrack = MutableLiveData<Track>()

    val currentTrack: LiveData<Track> = _currentTrack

    val listener = object : SpotifyRemoteManager.ViewModelListener {
        override fun onConnected() {
            Log.d(SpotifyRemoteViewModel::class.java.name, "onConnected")
        }

        override fun onFailure(error: Throwable) {
            Log.d(SpotifyRemoteViewModel::class.java.name, "onFailure:$error")
        }

        override fun onNextPlayerState(playerState: PlayerState) {
            if(_currentTrack.value?.uri != playerState.track?.uri){
                _currentTrack.value = playerState.track
                Log.d(SpotifyRemoteViewModel::class.java.name, "onNextPlayerState:$playerState")
            }
        }

        override fun onNextPlayerError(error: Throwable) {
            Log.d(SpotifyRemoteViewModel::class.java.name, "onNextPlayerError:$error")
        }
    }

    fun connect() {
        spotifyRemoteManager.addListener(listener)
        spotifyRemoteManager.connect()
    }

    fun disconnect() {
        spotifyRemoteManager.disconnect()
        spotifyRemoteManager.removeListener(listener)
    }
}