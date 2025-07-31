package com.example.neuroviewexam.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.neuroviewexam.R

val FunnelDisplayFontFamily = FontFamily(
    Font(R.font.funnel_display_variable, FontWeight.W100),
    Font(R.font.funnel_display_variable, FontWeight.W300),
    Font(R.font.funnel_display_variable, FontWeight.Normal),
    Font(R.font.funnel_display_variable, FontWeight.Medium),
    Font(R.font.funnel_display_variable, FontWeight.SemiBold),
    Font(R.font.funnel_display_variable, FontWeight.Bold),
    Font(R.font.funnel_display_variable, FontWeight.ExtraBold),
    Font(R.font.funnel_display_variable, FontWeight.Black)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FunnelDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FunnelDisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FunnelDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FunnelDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FunnelDisplayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FunnelDisplayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)