package com.erivera.apps.topcharts.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.SpotifyRemoteManager
import com.erivera.apps.topcharts.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.models.domain.AudioItem
import com.erivera.apps.topcharts.repository.Repository
import com.spotify.protocol.types.Album
import com.spotify.protocol.types.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.BR
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.roundToInt

class PlayerViewModel @Inject constructor(
    private val appContext: Context,
    private val repository: Repository,
    private val spotifyRemoteManager: SpotifyRemoteManager
) : ViewModel() {

    private val diffConfig =
        AsyncDifferConfig.Builder<AudioItem>(object : DiffUtil.ItemCallback<AudioItem>() {
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: AudioItem, newItem: AudioItem): Boolean {
                return oldItem.displayTitle == newItem.displayTitle && newItem.displayDescription == oldItem.displayDescription
            }

            override fun areItemsTheSame(oldItem: AudioItem, newItem: AudioItem): Boolean {
                return oldItem.displayTitle == newItem.displayTitle
            }
        }).build()

    private val _albumUrl = MutableLiveData<String>()

    val albumUrl: LiveData<String> = _albumUrl

    private val _trackTitle = MutableLiveData<String>()

    val trackTitle: LiveData<String> = _trackTitle

    private val _trackTitleColor = MutableLiveData<Int>().apply {
        value = ContextCompat.getColor(appContext, R.color.white)
    }

    val trackTitleColor: LiveData<Int> = _trackTitleColor

    private val _trackDescription = MutableLiveData<String>()

    val trackDescription: LiveData<String> = _trackDescription

    private val _albumColors = MutableLiveData<Array<Int>>()

    val albumColors: LiveData<Array<Int>> = _albumColors

    private val _isPlaying = MutableLiveData<Boolean>().apply {
        value = false
    }

    val playVisibility: LiveData<Int> = Transformations.map(_isPlaying) {
        if (it) View.VISIBLE else View.INVISIBLE
    }

    val pauseVisibility: LiveData<Int> = Transformations.map(_isPlaying) {
        if (it) View.INVISIBLE else View.VISIBLE
    }

    private val _audioItemList = AsyncDiffObservableList<AudioItem>(diffConfig)

    val audioItemList: AsyncDiffObservableList<AudioItem> = _audioItemList

    private var track: Track? = null
        set(value) {
            value?.let {
                updateTrackInfo(value)
            }

        }

    private val decimalFormat = DecimalFormat("#.##").apply {
        roundingMode = RoundingMode.CEILING
    }

    val listener = object : SpotifyRemoteManager.ViewModelListener {
        override fun onCurrentTrackChanged(track: Track) {
            this@PlayerViewModel.track = track
            Log.d(PlayerViewModel::class.java.name, "onCurrentTrackChanged:$track")
        }

        override fun onPauseStateChanged(isPaused: Boolean) {
            _isPlaying.value = isPaused
            Log.d(PlayerViewModel::class.java.name, "onPauseStateChanged:$isPaused")
        }
    }

    init {
        spotifyRemoteManager.addListener(listener)
    }

    private fun updateTrackInfo(track: Track) {
        _trackTitle.value = track.name
        _trackDescription.value =
            "${track.artists.joinToString(",", transform = { it.name })} - ${track.album.name}"
        updateAlbumInfo(track.album)
        updateAudioItemList(track.uri)
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

    private fun updateAudioItemList(uri: String?) {
        viewModelScope.launch {
            uri?.let {
                val trimmedTrackId = uri.removePrefix("spotify:track:")
                repository.getAudioFeatures(trimmedTrackId).let { audioFeatures ->
                    val audioList = createAudioItemList(audioFeatures)
                    withContext(Dispatchers.Main) {
                        _audioItemList.update(audioList)
                    }
                }
            }
        }
    }

    private fun createAudioItemList(audioFeaturesResponse: AudioFeaturesResponse): List<AudioItem> {
        return mutableListOf<AudioItem>().apply {
            add(
                AudioItem(
                    displayTitle = "Key",
                    secondaryDisplayTitle = null,
                    displayDescription = getPitchString(audioFeaturesResponse.key),
                    dialogText = appContext.resources.getString(R.string.key_description),
                    dialogDrawable = null
                )
            )
            add(
                AudioItem(
                    displayTitle = "Mode",
                    secondaryDisplayTitle = null,
                    displayDescription = if (audioFeaturesResponse.mode == 1) "Major" else "Minor",
                    dialogText = appContext.resources.getString(R.string.mode_description),
                    dialogDrawable = null
                )
            )
            add(
                AudioItem(
                    displayTitle = "Time",
                    secondaryDisplayTitle = "Signature",
                    displayDescription = audioFeaturesResponse.timeSignature.toString(),
                    dialogText = appContext.resources.getString(R.string.time_sig_description),
                    dialogDrawable = null
                )
            )
            add(
                AudioItem(
                    displayTitle = "BPM",
                    secondaryDisplayTitle = null,
                    displayDescription = (audioFeaturesResponse.tempo
                        ?: 0F).roundToInt().toString(),
                    dialogText = appContext.resources.getString(R.string.tempo_description),
                    dialogDrawable = R.drawable.graph_tempo
                )
            )
            add(
                AudioItem(
                    displayTitle = "Acousticness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.acousticness),
                    dialogText = appContext.resources.getString(R.string.acousticness_description),
                    dialogDrawable = R.drawable.graph_acousticness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Danceability",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.danceability),
                    dialogText = appContext.resources.getString(R.string.dancability_description),
                    dialogDrawable = R.drawable.graph_danceability
                )
            )
            add(
                AudioItem(
                    displayTitle = "Energy",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.energy),
                    dialogText = appContext.resources.getString(R.string.energy_description),
                    dialogDrawable = R.drawable.graph_energy
                )
            )
            add(
                AudioItem(
                    displayTitle = "Instrumentalness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.instrumentalness),
                    dialogText = appContext.resources.getString(R.string.instrumentalness_description),
                    dialogDrawable = R.drawable.graph_instrumentalness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Liveness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.liveness),
                    dialogText = appContext.resources.getString(R.string.liveness_description),
                    dialogDrawable = R.drawable.graph_liveness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Loudness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.loudness),
                    dialogText = appContext.resources.getString(R.string.loudness_description),
                    dialogDrawable = R.drawable.graph_loudness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Speechiness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.speechiness),
                    dialogText = appContext.resources.getString(R.string.speechiness_description),
                    dialogDrawable = R.drawable.graph_speechiness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Valence",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.valence),
                    dialogText = appContext.resources.getString(R.string.valence_description),
                    dialogDrawable = R.drawable.graph_valence
                )
            )
        }
    }

    private fun updateAudioItemList(audioFeaturesResponse: AudioFeaturesResponse): List<AudioItem> {
        return _audioItemList.toMutableList().apply {
            get(0).displayDescription = getPitchString(audioFeaturesResponse.key)
            get(1).displayDescription = if (audioFeaturesResponse.mode == 1) "Major" else "Minor"
            get(2).displayDescription = audioFeaturesResponse.timeSignature.toString()
            get(3).displayDescription = (audioFeaturesResponse.tempo ?: 0F).roundToInt().toString()
            get(4).displayDescription = decimalFormat.format(audioFeaturesResponse.acousticness)
            get(5).displayDescription = decimalFormat.format(audioFeaturesResponse.danceability)
            get(6).displayDescription = decimalFormat.format(audioFeaturesResponse.energy)
            get(7).displayDescription = decimalFormat.format(audioFeaturesResponse.instrumentalness)
            get(8).displayDescription = decimalFormat.format(audioFeaturesResponse.liveness)
            get(9).displayDescription = decimalFormat.format(audioFeaturesResponse.loudness)
            get(10).displayDescription = decimalFormat.format(audioFeaturesResponse.speechiness)
            get(11).displayDescription = decimalFormat.format(audioFeaturesResponse.valence)
        }
    }

    private fun getPitchString(index: Int?): String {
        var retVal = "N/A"
        if (index != null && index != -1) {
            val resId =
                appContext.resources.getIdentifier("pitch_$index", "string", appContext.packageName)
            retVal = appContext.resources.getString(resId)
        }
        return retVal
    }

    fun getItemBinding(): ItemBinding<AudioItem> {
        return ItemBinding.of(BR.audioItem, R.layout.view_grid_player_info)
    }

    fun analyzeDrawable(drawable: Drawable) {
        viewModelScope.launch(Dispatchers.IO) {
            val palette = Palette.from(drawable.toBitmap()).generate()
            withContext(Dispatchers.Main) {
                if (palette.swatches.isNotEmpty()) {
                    _albumColors.value = listOf(palette.lightVibrantSwatch, palette.vibrantSwatch, palette.darkVibrantSwatch).mapNotNull { it?.rgb }.toTypedArray()
                }
            }
        }
    }

    fun togglePlayPause() {
        spotifyRemoteManager.togglePlayPause()
    }

    fun next() {
        spotifyRemoteManager.next()
    }

    fun previous() {
        spotifyRemoteManager.previous()
    }

    override fun onCleared() {
        super.onCleared()
        spotifyRemoteManager.removeListener(listener)
    }
}