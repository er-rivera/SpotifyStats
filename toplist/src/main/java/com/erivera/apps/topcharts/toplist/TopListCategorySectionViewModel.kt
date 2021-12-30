package com.erivera.apps.topcharts.toplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.repository.Repository
import com.erivera.apps.topcharts.repository.persistance.artist.Artist
import com.erivera.apps.topcharts.repository.persistance.tracks.Track
import com.erivera.apps.topcharts.spotify.SpotifyRemoteManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopListCategorySectionViewModel @Inject constructor(
    val repository: Repository,
    val spotifyRemoteManager: SpotifyRemoteManager
) :
    ViewModel() {
    companion object {
        const val SONGS = "Songs"
        const val ARTISTS = "Artists"
        const val GENRES = "Genres"
    }

    var title = ""

    fun setCategorySection(title: String) {
        if (this.title != title) {
            this.title = title
            init(title)
        }
    }

    private val _state = MutableStateFlow<CategorySectionViewState?>(null)

    val state: StateFlow<CategorySectionViewState?>
        get() = _state

    private fun init(title: String) {
        viewModelScope.launch {
            val flow = when (title) {
                SONGS -> {
                    getSongCategorySectionViewState(isMock = false)
                }
                ARTISTS -> {
                    getArtistCategorySectionViewState(isMock = false)
                }
                else -> {
                    null
                }
            }
            flow?.collect {
                _state.value = it
            }
        }
    }

    private suspend fun getSongCategorySectionViewState(isMock: Boolean = false): Flow<CategorySectionViewState> {
        return if (isMock) {
            mockResults()
        } else {
            combine(
                repository.getShortTermTracks(),
                repository.getMediumTermTracks(),
                repository.getLongTermTracks()
            ) { shortTerm, medTerm, longTerm ->
                CategorySectionViewState(buildTracksCategorySection(shortTerm, medTerm, longTerm))
            }
        }
    }

    private fun buildTracksCategorySection(
        shortTermTracks: List<Track>,
        mediumTermTracks: List<Track>,
        longTermTracks: List<Track>
    ): MutableList<CategoryItem> {
        val categoryItems = mutableListOf<CategoryItem>()
        categoryItems.add(
            SubCategoryHeader("Short Term")
        )
        categoryItems.addAll(
            shortTermTracks.map { item ->
                SubCategoryItem.SongCategoryItem(
                    title = item.name,
                    artist = item.artistConcatenated,
                    position = item.currentShortTermPosition.toString(),
                    imageUrl = item.imageUrl,
                    uri = item.uri
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Medium Term")
        )
        categoryItems.addAll(
            mediumTermTracks.map { item ->
                SubCategoryItem.SongCategoryItem(
                    title = item.name,
                    artist = item.artistConcatenated,
                    position = item.currentMidTermPosition.toString(),
                    imageUrl = item.imageUrl,
                    uri = item.uri
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Long Term")
        )
        categoryItems.addAll(
            longTermTracks.map { item ->
                SubCategoryItem.SongCategoryItem(
                    title = item.name,
                    artist = item.artistConcatenated,
                    position = item.currentLongTermPosition.toString(),
                    imageUrl = item.imageUrl,
                    uri = item.uri
                )
            }
        )
        return categoryItems
    }

    private suspend fun getArtistCategorySectionViewState(isMock: Boolean = false): Flow<CategorySectionViewState> {
        return if (isMock) {
            mockResults()
        } else {
            combine(
                repository.getShortTermArtists(),
                repository.getMediumTermArtists(),
                repository.getLongTermArtists()
            ) { shortTerm, medTerm, longTerm ->
                CategorySectionViewState(buildArtistCategorySection(shortTerm, medTerm, longTerm))
            }
        }
    }

    private fun mockResults(): Flow<CategorySectionViewState> {
        val categoryItems = listOf(
            SubCategoryHeader("Short Term"),
            SubCategoryItem.SongCategoryItem(
                title = "Random 1",
                position = "1",
                artist = "Here is the artist",
                imageUrl = "",
                uri = ""
            ),
            SubCategoryItem.SongCategoryItem(
                title = "Random 2",
                position = "2",
                artist = "Here is the artist",
                imageUrl = "",
                uri = ""
            ),
            SubCategoryItem.SongCategoryItem(
                title = "Random 3",
                position = "3",
                imageUrl = "",
                uri = ""
            ),
        )
        return flow { emit(CategorySectionViewState(categoryItems)) }
    }

    private fun buildArtistCategorySection(
        shortTermArtists: List<Artist>,
        mediumTermArtists: List<Artist>,
        longTermArtists: List<Artist>
    ): MutableList<CategoryItem> {
        val categoryItems = mutableListOf<CategoryItem>()
        categoryItems.add(
            SubCategoryHeader("Short Term")
        )
        categoryItems.addAll(
            shortTermArtists.map { item ->
                SubCategoryItem.ArtistCategoryItem(
                    title = item.name,
                    position = item.currentShortTermPosition.toString(),
                    imageUrl = item.images,
                    uri = item.uri
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Medium Term")
        )
        categoryItems.addAll(
            mediumTermArtists.map { item ->
                SubCategoryItem.ArtistCategoryItem(
                    title = item.name,
                    position = item.currentMidTermPosition.toString(),
                    imageUrl = item.images,
                    uri = item.uri
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Long Term")
        )
        categoryItems.addAll(
            longTermArtists.map { item ->
                SubCategoryItem.ArtistCategoryItem(
                    title = item.name,
                    position = item.currentLongTermPosition.toString(),
                    imageUrl = item.images,
                    uri = item.uri
                )
            }
        )
        return categoryItems
    }

    fun play(uri: String) {
        if (uri.isNotEmpty()) {
            spotifyRemoteManager.play(uri)
        }
    }

    class CategorySectionViewState(val categoryList: List<CategoryItem>)

    interface CategoryItem

    data class SubCategoryHeader(
        val title: String
    ) : CategoryItem

    sealed class SubCategoryItem(
        val title: String,
        val artist: String? = null,
        val position: String,
        val imageUrl: String,
        val uri: String
    ) : CategoryItem {
        class SongCategoryItem(
            title: String,
            artist: String? = null,
            position: String,
            imageUrl: String,
            uri: String
        ) : SubCategoryItem(title, artist, position, imageUrl, uri)

        class ArtistCategoryItem(
            title: String,
            artist: String? = null,
            position: String,
            imageUrl: String,
            uri: String
        ) : SubCategoryItem(title, artist, position, imageUrl, uri)
    }
}