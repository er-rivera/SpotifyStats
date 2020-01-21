package com.erivera.apps.topcharts.viewmodels

import androidx.lifecycle.*
import com.erivera.apps.topcharts.StartupRouteState
import com.erivera.apps.topcharts.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(val repository : Repository) : ViewModel() {
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