package com.erivera.apps.topcharts.common_ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun getInconsolataTypography(): Typography {
    val inconsolata = FontFamily(
        Font(R.font.inconsolata_regular, FontWeight.Light),
        Font(R.font.inconsolata_regular, FontWeight.Normal),
        Font(R.font.inconsolata_semibold, FontWeight.SemiBold),
        Font(R.font.inconsolata_bold, FontWeight.Bold),
        Font(R.font.inconsolata_extrabold, FontWeight.ExtraBold)
    )

    return Typography(
        h1 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.Light,
            fontSize = 96.sp,
            letterSpacing = (-1.5).sp
        ),
        h6 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp
        ),
        h4 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.SemiBold,
            fontSize = 34.sp,
            letterSpacing = 0.25.sp
        ),
        h3 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.SemiBold,
            fontSize = 48.sp,
            letterSpacing = 0.sp
        ),
        caption = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp
        ),
        body1 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp
        ),
        body2 = TextStyle(
            fontFamily = inconsolata,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp
        )
    )
}