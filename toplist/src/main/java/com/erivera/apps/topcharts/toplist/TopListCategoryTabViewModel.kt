package com.erivera.apps.topcharts.toplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erivera.apps.topcharts.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopListCategoryTabViewModel @Inject constructor(val repository: Repository) : ViewModel() {
    private val _selectedTab = MutableStateFlow<CategoryTab?>(null)

    private val _state = MutableStateFlow<TopListTabViewState?>(null)

    val state: StateFlow<TopListTabViewState?>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                getCategorySectionsTabs().onEach {
                    if (_selectedTab.value == null) {
                        _selectedTab.value = it.first()
                    }
                },
                _selectedTab
            ) { categorySections, selectedCategory ->
                TopListTabViewState(
                    TopListTabs(
                        categoryTabList = categorySections,
                        selectedCategoryTab = selectedCategory
                    )
                )
            }.collect {
                _state.value = it
            }
        }
    }

    private fun getCategorySectionsTabs(): Flow<List<CategoryTab>> {
        val list = mutableListOf<CategoryTab>().apply {
            add(CategoryTab(TopListCategorySectionViewModel.SONGS))
            add(CategoryTab(TopListCategorySectionViewModel.ARTISTS))
        }
        return flow { emit(list) }
    }

    fun onTabSelected(tab: CategoryTab) {
        _selectedTab.value = tab
    }

    class TopListTabViewState(val tabs: TopListTabs)

    data class TopListTabs(
        val categoryTabList: List<CategoryTab>,
        val selectedCategoryTab: CategoryTab? = null
    )

    data class CategoryTab(val title: String)
}