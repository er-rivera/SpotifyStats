package com.erivera.apps.topcharts.ui.viewmodel

import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.DeviceManager
import com.erivera.apps.topcharts.spotify.Constants
import com.erivera.apps.topcharts.repository.Repository
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val deviceManager: DeviceManager
) : ViewModel() {

    companion object {
        val TAG: String = MainViewModel::class.java.name
    }

    private val _screenNavigationLiveData = MutableLiveData<Boolean>()

    val screenNavigationLiveData: LiveData<Boolean>
        get() = _screenNavigationLiveData

    fun getAuthenticationRequest(): AuthorizationRequest? {
        val builder = AuthorizationRequest.Builder(
            Constants.CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            Constants.REDIRECT_URI
        )
        builder.setScopes(
            arrayOf(
                "streaming",
                "user-read-recently-played",
                "user-top-read, app-remote-control"
            )
        )
        return builder.build()
    }

    fun updateDefaultWidthHeight(windowManager: WindowManager, statusBarHeight: Int) {
        deviceManager.setDefaultWidthHeight(
            windowManager = windowManager,
            statusBarHeight = statusBarHeight
        )
    }

    fun handleSuccessToken(accessToken: String) {
        saveSpotifyCredential(accessToken)
        viewModelScope.launch(Dispatchers.IO) {
            val refreshFlow = MutableStateFlow(false)
            repository.refreshDb(refreshFlow)
            refreshFlow.collect {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "handleSuccessToken: is DB Refreshed $it")
                    navigateToNextScreen()
                }
            }
        }
    }

    private fun saveSpotifyCredential(id: String) {
        repository.saveSpotifyClientId(id)
        repository.startService()
    }

    private fun navigateToNextScreen() {
        _screenNavigationLiveData.value = true
    }
}