package com.erivera.apps.topcharts.toplist

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TopListTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = TopListColors,
        content = content
    )
}