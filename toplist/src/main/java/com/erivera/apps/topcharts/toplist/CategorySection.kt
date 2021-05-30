package com.erivera.apps.topcharts.toplist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erivera.apps.topcharts.common_ui.getInconsolataTypography
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
            imageUrl = "",
        )
    )

}

@Composable
fun SubCategoryItemContent(item: TopListCategorySectionViewModel.SubCategoryItem) {
    val typography = getInconsolataTypography()
    ConstraintLayout( modifier = Modifier
        .fillMaxWidth()
        .height(85.dp)
        .padding(PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp))
    ) {
        val (position, image, title, description) = createRefs()
        Text(
            text = item.position,
            style = typography.h6,
            fontSize = 30.sp,
            modifier = Modifier
                .constrainAs(position) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        if (item.imageUrl.isNotEmpty()){
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
                    .constrainAs(image) {
                        start.linkTo(position.end, margin = 16.dp)
                    }
            )
        }
        Text(
            text = item.title,
            style = typography.h6,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                bottom.linkTo(description.top, margin = 2.dp)
                start.linkTo(if (item.imageUrl.isEmpty()) position.end else image.end, margin = 16.dp)
            }
        )
        Text(text = item.artist.orEmpty(),
            style = typography.caption,
            modifier = Modifier.constrainAs(description) {
                top.linkTo(title.bottom)
                start.linkTo(if (item.imageUrl.isEmpty()) position.end else image.end, margin = 16.dp)
                bottom.linkTo(parent.bottom)
            }
        )
        createVerticalChain(
            title,
            description,
            chainStyle = ChainStyle.Packed
        )
    }
}

@Composable
fun SubCategoryHeaderContent(title: String) {
    val typography = getInconsolataTypography()
    Text(text = title, style = typography.body1, modifier = Modifier.padding(8.dp))
}