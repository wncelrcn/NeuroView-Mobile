package com.example.neuroviewexam.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuroviewexam.R

@Composable
fun TopAppBar(
    backgroundColor: Color = Color.Black, // Changed to black to match the image
    textColor: Color = Color.White,     // Changed to white to match the image
    logoSize: Int = 36,                 // Adjusted for a slightly larger logo relative to text
    titleFontSize: Int = 24,            // Kept as is, matches visual
    barHeight: Int = 64,
    horizontalPadding: Int = 18,
    topPadding: Int = 10,        // Add vertical padding from top
    bottomPadding: Int = 0,     // Add vertical padding from bottom
    verticalOffset: Int = 0     // Add vertical offset to move entire bar up/down
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height((barHeight + topPadding + bottomPadding).dp)
            .offset(y = verticalOffset.dp), // Apply vertical offset
        color = backgroundColor,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = horizontalPadding.dp,
                    end = horizontalPadding.dp,
                    top = topPadding.dp,
                    bottom = bottomPadding.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.topbar_logo), // You can change this to your app logo
                contentDescription = "NeuroView Logo",
                modifier = Modifier
                    .size(logoSize.dp)
                    .padding(end = 8.dp)
            )

            // App Title
            Text(
                text = "NeuroView",
                fontSize = titleFontSize.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}