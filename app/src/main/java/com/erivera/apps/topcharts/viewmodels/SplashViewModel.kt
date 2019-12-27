package com.erivera.apps.topcharts.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.StartupRouteState
import com.erivera.apps.topcharts.repository.Repository
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository.getInstance(application)

    private val _navigationLiveData: MutableLiveData<StartupRouteState> = MutableLiveData()

    val navigationLiveData: LiveData<StartupRouteState>
        get() = _navigationLiveData

    fun checkLoginStatus(){
        repository.startService()
        viewModelScope.launch {
            if(repository.hasValidSpotifyClientId()){
                _navigationLiveData.postValue(StartupRouteState.Home)
            } else {
                _navigationLiveData.postValue(StartupRouteState.Login)
            }
        }
    }
}