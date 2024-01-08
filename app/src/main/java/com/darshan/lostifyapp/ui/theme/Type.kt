package com.darshan.lostifyapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.darshan.lostifyapp.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_light, weight = FontWeight.Light),
    Font(R.font.poppins_regular, style = FontStyle.Italic),
    Font(R.font.poppins_regular, weight = FontWeight.Bold),
    Font(R.font.poppins_regular, weight = FontWeight.Black)
)

val Billabong = FontFamily(
    Font(R.font.billabong_regular, weight = FontWeight.Bold)
)

val Typography = Typography(
    defaultFontFamily = Poppins
)