package com.erivera.apps.topcharts.toplist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TopList() {
    val viewModel = viewModel(TopListComposeViewModel::class.java)

    val viewState by viewModel.state.collectAsState()

    when(viewState){
        is TopListComposeViewModel.TopListViewState.Success -> {
            val title = (viewState as TopListComposeViewModel.TopListViewState.Success).topList.
                categorySectionList.firstOrNull()?.title ?: ""
            Surface(Modifier.fillMaxSize()) {
                TopListContent(title)
            }
        }
        else -> {
            Surface(Modifier.fillMaxSize()) {

            }
        }
    }
}

@Preview
@Composable
fun PreviewTopList(){
    TopListContent("new title")
}

@Composable
fun TopListContent(title: String = "" ) {
    Text(text = title)
}