package com.erivera.apps.topcharts.toplist

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TopList() {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth()) {
            TopListContent(modifier = Modifier.fillMaxWidth().weight(1F))
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
                onClick = { onCategoryTabSelected(categorySection) }) {
                TabContent(
                    index == selectedIndex,
                    text = categorySection.title,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun TabContent(selected: Boolean, text: String, modifier: Modifier) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
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