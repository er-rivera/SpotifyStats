package com.erivera.apps.topcharts.ui.viewmodel

import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.DeviceManager
import com.erivera.apps.topcharts.spotify.Constants
import com.erivera.apps.topcharts.repository.Repository
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: Repository, val deviceManager: DeviceManager) : ViewModel() {

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

    fun getAuthenticationRequest(): AuthorizationRequest? {
        val builder = AuthorizationRequest.Builder(
            Constants.CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            Constants.REDIRECT_URI
        )
        builder.setScopes(arrayOf("streaming", "user-read-recently-played", "user-top-read, app-remote-control"))
        return builder.build()
    }

    fun updateDefaultWidthHeight(windowManager: WindowManager, statusBarHeight: Int){
        deviceManager.setDefaultWidthHeight(windowManager = windowManager, statusBarHeight = statusBarHeight)
    }
}