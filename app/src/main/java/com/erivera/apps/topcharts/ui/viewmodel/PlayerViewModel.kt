package com.erivera.apps.topcharts.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.erivera.apps.topcharts.DeviceManager
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.spotify.SpotifyRemoteManager
import com.erivera.apps.topcharts.repository.models.api.AudioFeaturesResponse
import com.erivera.apps.topcharts.models.domain.AudioItem
import com.erivera.apps.topcharts.repository.Repository
import com.erivera.apps.topcharts.utils.getDimenPercentage
import com.spotify.protocol.types.Album
import com.spotify.protocol.types.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val repository: Repository,
    private val spotifyRemoteManager: SpotifyRemoteManager,
    private val deviceManager: DeviceManager
) : ViewModel() {

    companion object {
        val TAG = PlayerViewModel::class.java.name
    }

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
        value = ContextCompat.getColor(context, R.color.white)
    }

    val trackTitleColor: LiveData<Int> = _trackTitleColor

    private val _trackDescription = MutableLiveData<String>()

    val trackDescription: LiveData<String> = _trackDescription

    private val _albumColors = MutableLiveData<Array<Int>>()

    val albumColors: LiveData<Array<Int>> = _albumColors

    private val _isPlaying = MutableLiveData<Boolean>().apply {
        value = false
    }

    val playVisibility: LiveData<Float> = Transformations.map(_isPlaying) {
        Log.d(PlayerViewModel::class.java.name, "playVisibility: $it")
        if (it) 1F else 0F
    }

    val pauseVisibility: LiveData<Float> = Transformations.map(_isPlaying) {
        Log.d(PlayerViewModel::class.java.name, "pauseVisibility: ${it.not()}")
        if (it.not()) 1F else 0F
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

    private val _playerMaxHeight = MutableLiveData<Int>()

    val playerMaxHeight: LiveData<Int> = _playerMaxHeight

    private val _playerMinHeight = MutableLiveData<Int>()

    val playerMinHeight: LiveData<Int> = _playerMinHeight

    private val _albumArtBottomPosition = MutableLiveData<Float>()

    val albumArtBottomPosition: LiveData<Float> = _albumArtBottomPosition

    val listener = object : SpotifyRemoteManager.ViewModelListener {
        override fun onCurrentTrackChanged(track: Track) {
            this@PlayerViewModel.track = track
            Log.d(PlayerViewModel::class.java.name, "onCurrentTrackChanged:$track")
        }

        override fun onPauseStateChanged(isPaused: Boolean) {
            Log.d(PlayerViewModel::class.java.name, "onPauseStateChanged:$isPaused")
            _isPlaying.value = isPaused
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
                val image = album.images?.getOrNull(1)?.url
                    ?: album.images?.getOrNull(0)?.url.orEmpty()
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
                    dialogText = context.resources.getString(R.string.key_description),
                    dialogDrawable = null
                )
            )
            add(
                AudioItem(
                    displayTitle = "Mode",
                    secondaryDisplayTitle = null,
                    displayDescription = if (audioFeaturesResponse.mode == 1) "Major" else "Minor",
                    dialogText = context.resources.getString(R.string.mode_description),
                    dialogDrawable = null
                )
            )
            add(
                AudioItem(
                    displayTitle = "Time",
                    secondaryDisplayTitle = "Signature",
                    displayDescription = audioFeaturesResponse.timeSignature.toString(),
                    dialogText = context.resources.getString(R.string.time_sig_description),
                    dialogDrawable = null
                )
            )
            add(
                AudioItem(
                    displayTitle = "BPM",
                    secondaryDisplayTitle = null,
                    displayDescription = (audioFeaturesResponse.tempo
                        ?: 0F).roundToInt().toString(),
                    dialogText = context.resources.getString(R.string.tempo_description),
                    dialogDrawable = R.drawable.graph_tempo
                )
            )
            add(
                AudioItem(
                    displayTitle = "Acousticness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(
                        audioFeaturesResponse.acousticness ?: 0
                    ),
                    dialogText = context.resources.getString(R.string.acousticness_description),
                    dialogDrawable = R.drawable.graph_acousticness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Danceability",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(
                        audioFeaturesResponse.danceability ?: 0
                    ),
                    dialogText = context.resources.getString(R.string.dancability_description),
                    dialogDrawable = R.drawable.graph_danceability
                )
            )
            add(
                AudioItem(
                    displayTitle = "Energy",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.energy ?: 0),
                    dialogText = context.resources.getString(R.string.energy_description),
                    dialogDrawable = R.drawable.graph_energy
                )
            )
            add(
                AudioItem(
                    displayTitle = "Instrumentalness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(
                        audioFeaturesResponse.instrumentalness ?: 0
                    ),
                    dialogText = context.resources.getString(R.string.instrumentalness_description),
                    dialogDrawable = R.drawable.graph_instrumentalness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Liveness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.liveness ?: 0),
                    dialogText = context.resources.getString(R.string.liveness_description),
                    dialogDrawable = R.drawable.graph_liveness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Loudness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.loudness ?: 0),
                    dialogText = context.resources.getString(R.string.loudness_description),
                    dialogDrawable = R.drawable.graph_loudness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Speechiness",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(
                        audioFeaturesResponse.speechiness ?: 0
                    ),
                    dialogText = context.resources.getString(R.string.speechiness_description),
                    dialogDrawable = R.drawable.graph_speechiness
                )
            )
            add(
                AudioItem(
                    displayTitle = "Valence",
                    secondaryDisplayTitle = null,
                    displayDescription = decimalFormat.format(audioFeaturesResponse.valence ?: 0),
                    dialogText = context.resources.getString(R.string.valence_description),
                    dialogDrawable = R.drawable.graph_valence
                )
            )
        }
    }

    private fun getPitchString(index: Int?): String {
        var retVal = "N/A"
        if (index != null && index != -1) {
            val resId =
                context.resources.getIdentifier("pitch_$index", "string", context.packageName)
            retVal = context.resources.getString(resId)
        }
        return retVal
    }

    fun getItemBinding(): ItemBinding<AudioItem> {
        return ItemBinding.of<AudioItem>(BR.audioItem, R.layout.view_grid_player_info)
    }

    fun analyzeDrawable(drawable: Drawable) {
        viewModelScope.launch(Dispatchers.IO) {
            val palette = Palette.from(drawable.toBitmap()).generate()
            withContext(Dispatchers.Main) {
                if (palette.swatches.isNotEmpty()) {
                    _albumColors.value = listOf(
                        palette.lightVibrantSwatch,
                        palette.vibrantSwatch,
                        palette.darkVibrantSwatch
                    ).mapNotNull { it?.rgb }.toTypedArray()
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


    fun setPlayerDefaultValues() {
        val maxPercentage =
            context.resources.getDimenPercentage(R.dimen.player_max_height_percentage)
        val minPercentage =
            context.resources.getDimenPercentage(R.dimen.player_min_height_percentage)
        val guidelineBottomHeightPercentage =
            context.resources.getDimenPercentage(R.dimen.player_artwork_guideline_bottom)
        val maxHeight = (maxPercentage * deviceManager.getDeviceInfo().useableHeight).toInt()
        val minHeight = (minPercentage * deviceManager.getDeviceInfo().useableHeight).toInt()
        val guidelineBottomHeight =
            (guidelineBottomHeightPercentage * deviceManager.getDeviceInfo().useableHeight).toInt()
        Log.d(
            TAG,
            "setPlayerMaxHeightMinHeight:maxHeight $maxHeight, minHeight $minHeight, guidelineBottomHeight $guidelineBottomHeight"
        )
        _playerMaxHeight.value = maxHeight
        _playerMinHeight.value = minHeight
        _albumArtBottomPosition.value =
            context.resources.getDimension(R.dimen.player_artwork_guideline_bottom_margin)
    }
}