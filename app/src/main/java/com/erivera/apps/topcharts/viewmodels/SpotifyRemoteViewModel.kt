package com.erivera.apps.topcharts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.SpotifyRemoteManager
import javax.inject.Inject

class SpotifyRemoteViewModel @Inject constructor(private val spotifyRemoteManager: SpotifyRemoteManager) :
    ViewModel() {

    private val _connectionLiveData = MutableLiveData<String>()

    val connectionLiveData: LiveData<String> = _connectionLiveData

    fun connect() {
        _connectionLiveData.value = spotifyRemoteManager.getConnectionParams()
    }

    fun isEnabled(): Boolean {
        return _connectionLiveData.value != null
    }
}