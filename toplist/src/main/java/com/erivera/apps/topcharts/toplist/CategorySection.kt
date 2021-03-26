package com.erivera.apps.topcharts.toplist

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erivera.apps.topcharts.common_ui.getOutrunTypography
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun CategorySection(title: String, modifier: Modifier) {
    val viewModel = viewModel(TopListCategorySectionViewModel::class.java)
    viewModel.setCategorySection(title)

    val state by viewModel.state.collectAsState()
    Log.d("CategorySectionOne", "called ${state?.categoryList?.size}")

    state?.categoryList?.apply {
        LazyColumn(
            modifier = modifier,
            horizontalAlignment = Alignment.Start,
            contentPadding = PaddingValues(bottom = 168.dp)
        ) {
            items(this@apply) { item ->
                when (item) {
                    is TopListCategorySectionViewModel.SubCategoryHeader -> {
                        SubCategoryHeaderContent(item.title)
                    }
                    is TopListCategorySectionViewModel.SubCategoryItem -> {
                        SubCategoryItemContent(item)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSection() {
    SubCategoryItemContent(
        TopListCategorySectionViewModel.SubCategoryItem(
            title = "Beats and Percussion",
            artist = "EightOhEight",
            position = "1",
            imageUrl = ""
        )
    )

}

@Composable
fun SubCategoryItemContent(item: TopListCategorySectionViewModel.SubCategoryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp))
    ) {
        Text(
            text = item.position,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(PaddingValues(end = 8.dp, bottom = 4.dp))
                .width(40.dp)
        )
        if (item.imageUrl.isNotEmpty()) {
            Card(elevation = 4.dp, modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
                CoilImage(
                    data = item.imageUrl,
                    contentDescription = null,
                    fadeIn = true,
                    contentScale = ContentScale.Crop,
                    loading = { },
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }
        Column(modifier = Modifier
            .padding(PaddingValues(start = 8.dp))
            .fillMaxHeight()) {
            Text(text = item.title, style = MaterialTheme.typography.h6)
            item.artist?.run {
                Text(text = this, style = MaterialTheme.typography.overline)
            }
        }
    }
}

@Composable
fun SubCategoryHeaderContent(title: String) {
    val typography = getOutrunTypography()
    Text(text = title, style = typography.body1, modifier = Modifier.padding(8.dp))
}