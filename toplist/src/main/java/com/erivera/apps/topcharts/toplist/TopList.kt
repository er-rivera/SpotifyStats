package com.erivera.apps.topcharts.toplist

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erivera.apps.topcharts.common_ui.getInconsolataTypography

@Composable
fun TopList() {
    TopListTheme {
        Surface(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth()) {
                TopListContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                )
            }
        }
    }
}

@Composable
fun TopListContent(modifier: Modifier) {
    val viewModel = viewModel(TopListCategoryTabViewModel::class.java)
    val viewState by viewModel.state.collectAsState()

    val topListTabs = viewState?.tabs
    if (topListTabs !== null && topListTabs.categoryTabList.isNotEmpty() && topListTabs.selectedCategoryTab != null) {
        Column(modifier) {
            CategoryTabs(
                categoryTabs = topListTabs.categoryTabList,
                selectedTab = topListTabs.selectedCategoryTab,
                onCategoryTabSelected = viewModel::onTabSelected
            )
            Crossfade(
                targetState = topListTabs.selectedCategoryTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { categorySection ->
                CategorySection(
                    title = categorySection.title,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Composable
@Preview
fun CategoryPreview() {
    val songs = TopListCategoryTabViewModel.CategoryTab.Songs
    val artists = TopListCategoryTabViewModel.CategoryTab.Artists
    val list = mutableListOf<TopListCategoryTabViewModel.CategoryTab>().apply {
        add(songs)
        add(artists)
    }
    TopListTheme {
        CategoryTabs(
            categoryTabs = list,
            selectedTab = songs,
            onCategoryTabSelected = { }
        )
    }
}


@Composable
fun CategoryTabs(
    categoryTabs: List<TopListCategoryTabViewModel.CategoryTab>,
    selectedTab: TopListCategoryTabViewModel.CategoryTab,
    onCategoryTabSelected: (TopListCategoryTabViewModel.CategoryTab) -> Unit,
) {
    val selectedIndex = categoryTabs.indexOfFirst { it == selectedTab }
    TabRow(
        selectedTabIndex = selectedIndex,
        divider = {},
        indicator = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        categoryTabs.forEachIndexed { index, categorySection ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategoryTabSelected(categorySection) },
            ) {
                TabContent(
                    index == selectedIndex,
                    text = categorySection.title,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 40.dp)
                )
            }
        }
    }
}

@Composable
fun TabContent(selected: Boolean, text: String, modifier: Modifier) {
    val typography = getInconsolataTypography()
    Surface(
        color = when {
            selected -> Color(0xFF000000)
            else -> MaterialTheme.colors.background
        },
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = typography.body1,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}