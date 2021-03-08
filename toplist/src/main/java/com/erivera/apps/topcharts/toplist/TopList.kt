package com.erivera.apps.topcharts.toplist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TopList() {

    //val topListViewModel: TopListComposeViewModel = viewModel()

    Surface(Modifier.fillMaxSize()) {
        TopListContent()
    }
}

@Preview
@Composable
fun PreviewTopList(){
    TopListContent()
}

@Composable
fun TopListContent(){
    Text(text = "Sample")
}