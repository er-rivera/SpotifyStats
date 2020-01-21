package com.erivera.apps.topcharts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.repository.Repository
import com.spotify.sdk.android.authentication.AuthenticationResponse
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _authenticationResponseLiveData = MutableLiveData<AuthenticationResponse>()

    val authenticationResponseLiveData: LiveData<AuthenticationResponse>
        get() = _authenticationResponseLiveData

    fun saveSpotifyCredential(id: String) {
        repository.saveSpotifyClientId(id)
        repository.startService()
    }

    fun setAuthenticationResponse(authenticationResponse: AuthenticationResponse) {
        _authenticationResponseLiveData.value = authenticationResponse
    }
}