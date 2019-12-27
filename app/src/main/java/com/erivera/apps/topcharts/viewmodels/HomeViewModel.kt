package com.erivera.apps.topcharts.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.BR
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.models.domain.Artist
import com.erivera.apps.topcharts.models.domain.HomeTab
import com.erivera.apps.topcharts.models.domain.TopListHeader
import com.erivera.apps.topcharts.models.domain.TopListItem
import com.erivera.apps.topcharts.repository.Repository
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository.getInstance(application)

    private val _tabList = MutableLiveData<List<HomeTab>>()

    val tabList: LiveData<List<HomeTab>>
        get() = _tabList

    init {
        val list = mutableListOf<HomeTab>()
        list.add(HomeTab("Songs"))
        list.add(HomeTab("Artists"))
        list.add(HomeTab("Genres"))
        _tabList.value = list
    }

    fun loadItems() {

        viewModelScope.launch {
            val artistList = mutableListOf<TopListItem>()
            with(artistList) {
                addArtistsSection(repository.getShortTermArtists(), this, "Short Term (4 Weeks)")
                addArtistsSection(repository.getMediumTermArtists(), this, "Mid Term (6 Months)")
                addArtistsSection(repository.getLongTermArtists(), this, "Long Term (Years)")
            }
            swapOutList("Artists", artistList)
            Log.i("sad", "safaedf")
        }
    }

    private fun swapOutList(title: String, newList: MutableList<TopListItem>) {
        _tabList.value?.find { it.title == title }?.list?.postValue(newList)
    }

    private fun addArtistsSection(
        responseList: List<ArtistsRetrofit>,
        masterList: MutableList<TopListItem>,
        headerString: String
    ) {
        with(masterList) {
            if (responseList.isNotEmpty()) {
                add(TopListHeader(headerString))
                responseList.mapIndexed { index, artistsRetrofit ->
                    add(transformArtist(artistsRetrofit, index + 1))
                }
            }
        }
    }

    private fun transformArtist(artistsRetrofit: ArtistsRetrofit, position: Int): Artist {
        return Artist(
            name = artistsRetrofit.name ?: "",
            photoUrl = artistsRetrofit.images?.firstOrNull()?.url ?: "",
            position = position
        )
    }

    fun getTabBinding(): ItemBinding<HomeTab> {
        return ItemBinding.of(BR.homeTab, R.layout.view_top_tab)
    }
}