package com.erivera.apps.topcharts.common_ui

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun getOutrunTypography(): Typography {
    val outrun = FontFamily(
        Font(R.font.outrun_future),
        Font(R.font.outrun_future_bold, FontWeight.Bold),
        Font(R.font.outrun_future_bold_italic, FontWeight.ExtraBold)
    )

    return Typography(
        h1 = TextStyle(
            fontFamily = outrun,
            fontWeight = FontWeight.Bold,
            fontSize = 96.sp
        ),
        body1 = TextStyle(
            fontFamily = outrun,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
}