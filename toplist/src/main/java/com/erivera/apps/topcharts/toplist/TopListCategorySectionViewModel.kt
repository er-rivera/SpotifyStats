package com.erivera.apps.topcharts.toplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import com.erivera.apps.topcharts.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopListCategorySectionViewModel @Inject constructor(val repository: Repository) :
    ViewModel() {
    companion object {
        const val SONGS = "Songs"
        const val ARTISTS = "Artists"
        const val GENRES = "Genres"
    }

    var title = ""

    fun setCategorySection(title: String) {
        if(this.title != title){
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
                    getSongCategorySectionViewState()
                }
                ARTISTS -> {
                    getArtistCategorySectionViewState()
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

    private suspend fun getSongCategorySectionViewState(): Flow<CategorySectionViewState> {
        return combine(
            repository.getShortTermTracks(),
            repository.getMediumTermTracks(),
            repository.getLongTermTracks()
        ) { shortTerm, medTerm, longTerm ->
            CategorySectionViewState(buildTracksCategorySection(shortTerm, medTerm, longTerm))
        }
    }

    private fun buildTracksCategorySection(
        shortTermTracks: List<TrackRetrofit>,
        mediumTermTracks: List<TrackRetrofit>,
        longTermTracks: List<TrackRetrofit>
    ): MutableList<CategoryItem> {
        val categoryItems = mutableListOf<CategoryItem>()
        categoryItems.add(
            SubCategoryHeader("Short Term")
        )
        categoryItems.addAll(
            shortTermTracks.mapIndexed { index, item ->
                SubCategoryItem(
                    title = item.name ?: "",
                    artist = item.artists?.joinToString(", "),
                    position = (index + 1).toString(),
                    imageUrl = item.album?.images?.first()?.url ?: ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Medium Term")
        )
        categoryItems.addAll(
            mediumTermTracks.mapIndexed { index, item ->
                SubCategoryItem(
                    title = item.name ?: "",
                    artist = item.artists?.joinToString(", "),
                    position = (index + 1).toString(),
                    imageUrl = item.album?.images?.first()?.url ?: ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Long Term")
        )
        categoryItems.addAll(
            longTermTracks.mapIndexed { index, item ->
                SubCategoryItem(
                    title = item.name ?: "",
                    artist = item.artists?.joinToString(", "),
                    position = (index + 1).toString(),
                    imageUrl = item.album?.images?.first()?.url ?: ""
                )
            }
        )
        return categoryItems
    }

    private suspend fun getArtistCategorySectionViewState(): Flow<CategorySectionViewState> {
        return combine(
            repository.getShortTermArtists(),
            repository.getMediumTermArtists(),
            repository.getLongTermArtists()
        ) { shortTerm, medTerm, longTerm ->
            CategorySectionViewState(buildArtistCategorySection(shortTerm, medTerm, longTerm))
        }
    }

    private fun buildArtistCategorySection(
        shortTermArtists: List<ArtistsRetrofit>,
        mediumTermArtists: List<ArtistsRetrofit>,
        longTermArtists: List<ArtistsRetrofit>
    ): MutableList<CategoryItem> {
        val categoryItems = mutableListOf<CategoryItem>()
        categoryItems.add(
            SubCategoryHeader("Short Term")
        )
        categoryItems.addAll(
            shortTermArtists.mapIndexed { index, item ->
                SubCategoryItem(
                    title = item.name ?: "",
                    position = (index + 1).toString(),
                    imageUrl = ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Medium Term")
        )
        categoryItems.addAll(
            mediumTermArtists.mapIndexed { index, item ->
                SubCategoryItem(
                    title = item.name ?: "",
                    position = (index + 1).toString(),
                    imageUrl = ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Long Term")
        )
        categoryItems.addAll(
            longTermArtists.mapIndexed { index, item ->
                SubCategoryItem(
                    title = item.name ?: "",
                    position = (index + 1).toString(),
                    imageUrl = ""
                )
            }
        )
        return categoryItems
    }

    class CategorySectionViewState(val categoryList: List<CategoryItem>)

    interface CategoryItem

    data class SubCategoryHeader(
        val title: String
    ) : CategoryItem

    data class SubCategoryItem(
        val title: String,
        val artist: String? = null,
        val position: String,
        val imageUrl: String
    ) : CategoryItem
}