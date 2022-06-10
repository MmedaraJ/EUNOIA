package com.example.eunoia.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.eunoia.R

val bioRhymeFonts = FontFamily(
    Font(R.font.biorhyme_regular, weight = FontWeight.Normal),
    Font(R.font.biorhyme_bold1, weight =  FontWeight.Bold),
    Font(R.font.biorhyme_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.biorhyme_light, weight =  FontWeight.Light),
    Font(R.font.biorhyme_extralight, weight = FontWeight.ExtraLight)
)

val morgyFonts = FontFamily(
    Font(R.font.morgy_regular, weight = FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = bioRhymeFonts,
        fontWeight = FontWeight.ExtraBold
    ),
    h2 = TextStyle(
        fontFamily = bioRhymeFonts,
        fontWeight = FontWeight.Bold
    ),
    h3 = TextStyle(
        fontFamily = bioRhymeFonts,
        fontWeight = FontWeight.Normal
    ),
    h4 = TextStyle(
        fontFamily = bioRhymeFonts,
        fontWeight = FontWeight.Light
    ),
    h5 = TextStyle(
        fontFamily = bioRhymeFonts,
        fontWeight = FontWeight.ExtraLight
    ),
    body1 = TextStyle(
        fontFamily = morgyFonts,
        fontWeight = FontWeight.Normal
    )
)