package com.erivera.apps.topcharts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.repository.Repository
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _screenNavigationLiveData = MutableLiveData<Boolean>()

    val screenNavigationLiveData: LiveData<Boolean>
        get() = _screenNavigationLiveData

    fun saveSpotifyCredential(id: String) {
        repository.saveSpotifyClientId(id)
        repository.startService()
    }

    fun navigateToNextScreen() {
        _screenNavigationLiveData.value = true
    }

    fun getAuthenticationRequest(): AuthenticationRequest? {
        val builder = AuthenticationRequest.Builder(
            CLIENT_ID,
            AuthenticationResponse.Type.TOKEN,
            REDIRECT_URI
        )
        builder.setScopes(arrayOf("streaming", "user-read-recently-played", "user-top-read, app-remote-control"))
        return builder.build()
    }

    companion object {
        private const val REDIRECT_URI = "https://github.com/er-rivera/SpotifyStats/"
        private const val CLIENT_ID = "8796497bbe804de1af6eb358bdcdc53f"
    }
}