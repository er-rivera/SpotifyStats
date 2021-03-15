package com.erivera.apps.topcharts.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.models.api.TrackRetrofit
import com.erivera.apps.topcharts.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopListComposeViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    companion object {
        const val SONGS = "Songs"
        const val ARTISTS = "Artists"
        const val GENRES = "Genres"
    }

    private val _selectedCategory = MutableStateFlow<CategorySection?>(null)

    private val _state = MutableStateFlow<TopListViewState>(TopListViewState.Loading)

    val state: StateFlow<TopListViewState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                repository.getShortTermTracks(),
                repository.getMediumTermTracks(),
                repository.getLongTermTracks(),
                repository.getShortTermArtists(),
                repository.getMediumTermArtists(),
                repository.getLongTermArtists(),
                _selectedCategory
            ) { shortTermTracks, mediumTermTracks, longTermTracks,
                shortTermArtists, mediumTermArtists, longTermArtists, selectedCategory ->
                val categorySections = mutableListOf<CategorySection>().apply {
                    add(
                        generateTracksCategorySection(
                            shortTermTracks,
                            mediumTermTracks,
                            longTermTracks
                        )
                    )
                    add(
                        generateArtistCategorySection(
                            shortTermArtists,
                            mediumTermArtists,
                            longTermArtists
                        )
                    )
                }
                TopListViewState.Success(
                    TopList(
                        categorySectionList = categorySections,
                        categorySections.first()
                    )
                )
            }.collect {
                _state.value = it
            }
        }
    }

    private fun generateTracksCategorySection(
        shortTermTracks: List<TrackRetrofit>,
        mediumTermTracks: List<TrackRetrofit>,
        longTermTracks: List<TrackRetrofit>
    ): CategorySection {
        val categoryItems = mutableListOf<CategoryItem>()
        categoryItems.add(
            SubCategoryHeader("Short Term")
        )
        categoryItems.addAll(
            shortTermTracks.mapIndexed { index, item ->
                SubCategoryItem(
                    item.name ?: "",
                    (index + 1).toString(),
                    ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Medium Term")
        )
        categoryItems.addAll(
            mediumTermTracks.mapIndexed { index, item ->
                SubCategoryItem(
                    item.name ?: "",
                    (index + 1).toString(),
                    ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Long Term")
        )
        categoryItems.addAll(
            longTermTracks.mapIndexed { index, item ->
                SubCategoryItem(
                    item.name ?: "",
                    (index + 1).toString(),
                    ""
                )
            }
        )
        return CategorySection(SONGS, categoryItems)
    }

    private fun generateArtistCategorySection(
        shortTermArtists: List<ArtistsRetrofit>,
        mediumTermArtists: List<ArtistsRetrofit>,
        longTermArtists: List<ArtistsRetrofit>
    ): CategorySection {
        val categoryItems = mutableListOf<CategoryItem>()
        categoryItems.add(
            SubCategoryHeader("Short Term")
        )
        categoryItems.addAll(
            shortTermArtists.mapIndexed { index, item ->
                SubCategoryItem(
                    item.name ?: "",
                    (index + 1).toString(),
                    ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Medium Term")
        )
        categoryItems.addAll(
            mediumTermArtists.mapIndexed { index, item ->
                SubCategoryItem(
                    item.name ?: "",
                    (index + 1).toString(),
                    ""
                )
            }
        )
        categoryItems.add(
            SubCategoryHeader("Long Term")
        )
        categoryItems.addAll(
            longTermArtists.mapIndexed { index, item ->
                SubCategoryItem(
                    item.name ?: "",
                    (index + 1).toString(),
                    ""
                )
            }
        )
        return CategorySection(ARTISTS, categoryItems)
    }

    /**
     * Returns a [Flow] whose values are generated with [transform] function by combining
     * the most recently emitted values by each flow.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
        flow7: Flow<T7>,
        transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
    ): Flow<R> = combine(flow, flow2, flow3, flow4, flow5, flow6, flow7) { args: Array<*> ->
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[3] as T5,
            args[3] as T6,
            args[3] as T7
        )
    }

    sealed class TopListViewState {
        class Success(topList: TopList) : TopListViewState()
        object Error : TopListViewState()
        object Loading : TopListViewState()
    }

    data class TopList(
        val categorySectionList: List<CategorySection>,
        val selectedCategorySection: CategorySection? = null
    )

    data class CategorySection(
        val title: String,
        val items: List<CategoryItem>
    )

    interface CategoryItem

    data class SubCategoryHeader(
        val title: String
    ) : CategoryItem

    data class SubCategoryItem(
        val title: String,
        val position: String,
        val imageUrl: String
    ) : CategoryItem
}