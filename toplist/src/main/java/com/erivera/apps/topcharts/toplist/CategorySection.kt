package com.erivera.apps.topcharts.toplist

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CategorySection(title: String, modifier: Modifier) {
    val viewModel = viewModel(TopListCategorySectionViewModel::class.java)
    viewModel.setCategorySection(title)

    val state by viewModel.state.collectAsState()
    Log.d("CategorySectionOne", "called ${state?.categoryList?.size}")

    state?.categoryList?.apply {
        LazyColumn(modifier = modifier, horizontalAlignment = Alignment.Start, contentPadding = PaddingValues(bottom = 168.dp)) {
            items(this@apply) { item ->
                when (item) {
                    is TopListCategorySectionViewModel.SubCategoryHeader -> {
                        SubCategoryHeaderContent(item.title)
                    }
                    is TopListCategorySectionViewModel.SubCategoryItem -> {
                        Text(item.title)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSection(){
    SubCategoryHeaderContent("Title")
}

@Composable
fun SubCategoryHeaderContent(title: String){
    Text(title)
}