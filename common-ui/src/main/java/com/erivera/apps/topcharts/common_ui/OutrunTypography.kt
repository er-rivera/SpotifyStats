package com.erivera.apps.topcharts.common_ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun getInconsolataTypography(): Typography {
    val inconsolata = FontFamily(
        Font(R.font.inconsolata_regular),
        Font(R.font.inconsolata_bold, FontWeight.Bold),
        Font(R.font.inconsolata_extrabold, FontWeight.ExtraBold)
    )

    return Typography(
        h1 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.Bold,
            fontSize = 96.sp
        ),
        body1 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
}