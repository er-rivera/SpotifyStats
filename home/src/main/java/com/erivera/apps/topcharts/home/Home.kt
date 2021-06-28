package com.erivera.apps.topcharts.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Home() {
    Surface(
        Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxWidth()) {
            Text(text = "Hello")
        }
    }
}
