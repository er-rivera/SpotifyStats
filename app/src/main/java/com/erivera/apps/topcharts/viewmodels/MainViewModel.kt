package com.erivera.apps.topcharts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erivera.apps.topcharts.repository.Repository
import com.spotify.sdk.android.authentication.AuthenticationResponse

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository.getInstance(application)

    private val _authenticationResponseLiveData = MutableLiveData<AuthenticationResponse>()

    val authenticationResponseLiveData: LiveData<AuthenticationResponse>
        get() = _authenticationResponseLiveData

    fun saveSpotifyCredential(id: String) {
        repository.saveSpotifyClientId(id)
        repository.startService()
    }

    fun setAuthenticationResponse(authenticationResponse: AuthenticationResponse){
        _authenticationResponseLiveData.value = authenticationResponse
    }
}