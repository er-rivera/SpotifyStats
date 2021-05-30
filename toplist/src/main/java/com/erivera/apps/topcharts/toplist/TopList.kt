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
private fun CategoryTabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: TopListCategoryTabViewModel.CategoryTab
) {
    val transition = updateTransition(
        tabPage,
        label = "Tab indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (TopListCategoryTabViewModel.CategoryTab.Songs isTransitioningTo TopListCategoryTabViewModel.CategoryTab.Artists) {
                // Indicator moves to the right.
                // Low stiffness spring for the left edge so it moves slower than the right edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                // Indicator moves to the left.
                // Medium stiffness spring for the left edge so it moves faster than the right edge.
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator left"
    ) { page ->
        tabPositions[page.ordinal].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (TopListCategoryTabViewModel.CategoryTab.Songs isTransitioningTo TopListCategoryTabViewModel.CategoryTab.Artists) {
                // Indicator moves to the right
                // Medium stiffness spring for the right edge so it moves faster than the left edge.
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                // Indicator moves to the left.
                // Low stiffness spring for the right edge so it moves slower than the left edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.ordinal].right
    }
    val color by transition.animateColor(
        label = "Border color"
    ) { page ->
        if (page == TopListCategoryTabViewModel.CategoryTab.Songs) Yellow800 else Red300
    }
    Box(
        Modifier
            .fillMaxHeight(0.5f)
            .wrapContentSize(align = Alignment.CenterStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .height(32.dp)
            .padding(horizontal = 6.dp, vertical = 0.dp)
            .border(
                BorderStroke(2.dp, color),
                RoundedCornerShape(4.dp)
            )
            .background(color = color, shape = RoundedCornerShape(4.dp))
    )
}


@Composable
fun CategoryTabs(
    categoryTabs: List<TopListCategoryTabViewModel.CategoryTab>,
    selectedTab: TopListCategoryTabViewModel.CategoryTab,
    onCategoryTabSelected: (TopListCategoryTabViewModel.CategoryTab) -> Unit,
) {
    val selectedIndex = categoryTabs.indexOfFirst { it == selectedTab }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        divider = {},
        edgePadding = 24.dp,
        indicator = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        categoryTabs.forEachIndexed { index, categorySection ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategoryTabSelected(categorySection) }
            ) {
                TabContent(
                    index == selectedIndex,
                    text = categorySection.title,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 48.dp)
                )
            }
        }
    }
}

@Composable
fun TabContent(selected: Boolean, text: String, modifier: Modifier) {
    Surface(
        color = when {
            selected -> Color(0xFF000000)
            else -> MaterialTheme.colors.background
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}