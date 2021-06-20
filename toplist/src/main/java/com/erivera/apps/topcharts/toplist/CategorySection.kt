package com.erivera.apps.topcharts.toplist

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erivera.apps.topcharts.common_ui.getInconsolataTypography
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Composable
fun CategorySection(title: String, modifier: Modifier) {
    val viewModel = viewModel(TopListCategorySectionViewModel::class.java)
    viewModel.setCategorySection(title)

    val state by viewModel.state.collectAsState()
    Log.d("CategorySectionOne", "called ${state?.categoryList?.size}")
    val context = LocalContext.current
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
                        SubCategoryItemContent(item) {
                            when (item) {
                                is TopListCategorySectionViewModel.SubCategoryItem.SongCategoryItem -> {
                                    viewModel.play(item.uri)
                                }
                                is TopListCategorySectionViewModel.SubCategoryItem.ArtistCategoryItem -> {
                                    goToArtistPage(context, item.uri)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun goToArtistPage(context: Context, artistPageUri: String){
    val uri = Uri.parse(artistPageUri)
    val intent = Intent(Intent.ACTION_VIEW, uri)

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.d("goToArtistPage", e.stackTraceToString())
    }
}

@Preview
@Composable
fun PreviewSection() {
    SubCategoryItemContent(
        TopListCategorySectionViewModel.SubCategoryItem.SongCategoryItem(
            title = "Beats and Percussion",
            artist = "EightOhEight",
            position = "1",
            imageUrl = "",
            uri = ""
        )
    ) {

    }

}

@Composable
fun SubCategoryItemContent(
    item: TopListCategorySectionViewModel.SubCategoryItem,
    onClick: () -> Unit
) {
    val typography = getInconsolataTypography()
    val painter = rememberCoilPainter(item.imageUrl, fadeIn = true)
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(color = Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            .height(84.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Image(
                painter = painter, contentDescription = "",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .height(84.dp)
                    .width(84.dp),
                contentScale = ContentScale.Crop
            )
            when (painter.loadState) {
                is ImageLoadState.Loading -> {
                    // Display a circular progress indicator whilst loading
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is ImageLoadState.Error -> {
                    // If you wish to display some content if the request fails
                }
                else -> {

                }
            }
        }
        Row {
            Column(modifier = Modifier.weight(7f)) {
                Text(
                    text = item.title,
                    style = typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.artist.orEmpty(),
                    style = typography.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = item.position,
                style = typography.h3,
                fontSize = 30.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SubCategoryHeaderContent(title: String) {
    val typography = getInconsolataTypography()
    Text(
        text = title,
        style = typography.h4,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
    )
}