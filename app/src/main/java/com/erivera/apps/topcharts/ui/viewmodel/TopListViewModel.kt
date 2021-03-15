package com.erivera.apps.topcharts.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.erivera.apps.topcharts.BR
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import com.erivera.apps.topcharts.models.domain.*
import com.erivera.apps.topcharts.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList
import javax.inject.Inject

@HiltViewModel
class TopListViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    companion object {
        const val SONGS = "Songs"
        const val ARTISTS = "Artists"
        const val GENRES = "Genres"
    }

    private val diffConfig =
        AsyncDifferConfig.Builder<TopListTab>(object : DiffUtil.ItemCallback<TopListTab>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: TopListTab, newItem: TopListTab): Boolean {
                return oldItem.id == newItem.id && newItem.list == oldItem.list
            }

            override fun areItemsTheSame(oldItem: TopListTab, newItem: TopListTab): Boolean {
                return oldItem.id == newItem.id
            }
        }).build()

    private val _tabList = AsyncDiffObservableList<TopListTab>(diffConfig)

    val tabList: AsyncDiffObservableList<TopListTab>
        get() = _tabList

    init {
        val list = mutableListOf<TopListTab>()
        list.add(TopListTab(SONGS))
        list.add(TopListTab(ARTISTS))
        list.add(TopListTab(GENRES))
        _tabList.update(list)
    }

    fun loadItems() {

        viewModelScope.launch {
            delay(1000)
            val currentList = mutableListOf<TopListTab>().apply {
                _tabList.map { value -> this.add(value.copy()) }
            }
            val artistList = mutableListOf<TopListItem>()
            with(artistList) {
                //TODO: make term amount dynamic
                add(TopListHeader("Header"))
                add(Artist("0", "Here is A Title", "", 0, ""))
                delay(1000)
//                addArtistsSection(repository.getShortTermArtists(), this, "Short Term (4 Weeks)")
//                addArtistsSection(repository.getMediumTermArtists(), this, "Mid Term (6 Months)")
//               addArtistsSection(repository.getLongTermArtists(), this, "Long Term (Years)")
            }
            swapOutList(ARTISTS, artistList, currentList)

            val songList = mutableListOf<TopListItem>()
            with(songList) {
                add(TopListHeader("Header"))
                add(Song("1", "Here is A Title", "", "", 1, ""))
                delay(1000)
//                addTracksSection(repository.getShortTermTracks(), this, "Short Term (4 Weeks)")
//                addTracksSection(repository.getMediumTermTracks(), this, "Mid Term (6 Months)")
//                addTracksSection(repository.getLongTermTracks(), this, "Long Term (Years)")
            }
            swapOutList(SONGS, songList, currentList)
            _tabList.update(currentList)
        }
    }

    fun getTabName(position: Int): String {
        return tabList.getOrNull(position)?.title ?: "Unknown"
    }

    fun getHomeTitle(): String {
        return "Your Top"
    }

    private fun swapOutList(
        title: String,
        newList: MutableList<TopListItem>,
        currentList: MutableList<TopListTab>
    ) {
        val item = currentList.find { it.title == title }
        item?.list = MutableLiveData<List<TopListItem>>().apply {
            value = newList
        }
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
            id = artistsRetrofit.id ?: "",
            name = artistsRetrofit.name ?: "",
            photoUrl = artistsRetrofit.images?.firstOrNull()?.url ?: "",
            position = position,
            uri = artistsRetrofit.uri ?: ""
        )
    }

    private fun addTracksSection(
        responseList: List<TrackRetrofit>,
        masterList: MutableList<TopListItem>,
        headerString: String
    ) {
        with(masterList) {
            if (responseList.isNotEmpty()) {
                add(TopListHeader(headerString))
                responseList.mapIndexed { index, trackRetrofit ->
                    add(transformTrack(trackRetrofit, index + 1))
                }
            }
        }
    }

    private fun transformTrack(trackRetrofit: TrackRetrofit, position: Int): Song {
        val artistName = trackRetrofit.artists?.map { it.name }?.joinToString(",") ?: ""
        val albumName = trackRetrofit.album?.name ?: "Unknown"
        return Song(
            id = trackRetrofit.id ?: "",
            titleName = trackRetrofit.name ?: "",
            description = "$artistName • $albumName",
            photoUrl = trackRetrofit.album?.images?.first()?.url ?: "",
            position = position,
            uri = trackRetrofit.uri ?: ""
        )
    }

    fun getTabBinding(): ItemBinding<TopListTab> {
        return ItemBinding.of(BR.topListTab, R.layout.view_top_tab)
    }
}