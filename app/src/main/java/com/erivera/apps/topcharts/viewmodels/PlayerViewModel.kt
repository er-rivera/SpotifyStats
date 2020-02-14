package com.erivera.apps.topcharts.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.repository.Repository
import com.spotify.protocol.types.Album
import com.spotify.protocol.types.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _nowPlaying = MutableLiveData<String>()

    val nowPlaying: LiveData<String> = _nowPlaying

    private val _playlistName = MutableLiveData<String>()

    val playlistName: LiveData<String> = _playlistName

    private val _albumUrl = MutableLiveData<String>()

    val albumUrl: LiveData<String> = _albumUrl

    private val _trackTitle = MutableLiveData<String>()

    val trackTitle: LiveData<String> = _trackTitle

    private val _trackDescription = MutableLiveData<String>()

    val trackDescription: LiveData<String> = _trackDescription

    private val _isPlaying = MutableLiveData<Boolean>()

    val isPlaying: LiveData<Boolean> = _isPlaying

    var track : Track? = null

    fun updateTrackInfo(track: Track) {
        this.track = track
        _nowPlaying.value = "Now Playing:"
        _playlistName.value = "N/A"
        _trackTitle.value = track.name
        _trackDescription.value =
            "${track.artists.joinToString(",", transform = { it.name })} - ${track.album.name}"
        updateAlbumInfo(track.album)
    }

    fun updatePlayState(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    private fun updateAlbumInfo(album: Album) {
        viewModelScope.launch {
            val trimmedAlbumId = album.uri.removePrefix("spotify:album:")
            repository.getAlbum(trimmedAlbumId).let { album ->
                val image = repository.getAlbum(trimmedAlbumId).images?.getOrNull(1)?.url
                    ?: album.images?.getOrNull(0)?.url
                withContext(Dispatchers.Main) {
                    Log.d("asdas", "sadsd")
                    _albumUrl.value = image
                }
            }
        }
    }

    fun getUri() : String? {
        return track?.uri
    }
}