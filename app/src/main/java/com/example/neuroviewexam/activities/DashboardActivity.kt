package com.example.neuroviewexam.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuroviewexam.components.BottomNavigationBar
import com.example.neuroviewexam.components.TopAppBar
import com.example.neuroviewexam.R
import com.example.neuroviewexam.data.TumorData
import com.example.neuroviewexam.ui.theme.NeuroViewExamTheme


import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.HorizontalPagerIndicator

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroViewExamTheme {
                DashboardScreen()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val tumors = TumorData.getAllTumors()

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                textColor = Color.White,
                logoSize = 42,
                titleFontSize = 28
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentActivityName = context::class.java.simpleName,
                bottomPadding = 18,
                navBarHeight = 80,
                regularIconSize = 32,
                fabIconSize = 32,
                fabSize = 64,
                navBarWidth = 0.8f,
                horizontalPadding = 16
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LEARN",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF737373),
                modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
            )

            val pagerState = rememberPagerState(initialPage = 0)

            HorizontalPager(
                count = tumors.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                itemSpacing = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { page ->
                val tumor = tumors[page]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.Transparent, RoundedCornerShape(16.dp))
                        .clickable {
                            val intent = Intent(context, TumorDetailActivity::class.java)
                            intent.putExtra("tumor_name", tumor.name)
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0B0B0B))
                            .padding(2.dp)
                            .background(Color(0xFF0B0B0B), RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painter = painterResource(id = tumor.imageResource),
                                contentDescription = "${tumor.name} image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .weight(0.3f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tumor.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_readmore),
                                        contentDescription = "Go to details",
                                        tint = Color.Black,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                activeColor = Color.White,
                inactiveColor = Color.Gray,
                indicatorWidth = 8.dp,
                indicatorHeight = 8.dp,
                spacing = 8.dp
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = buildAnnotatedString {
                    append("Our brain is one of the most ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("important and complex parts of the human body.")
                    }
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    append("It controls how we think, feel, move, and live. That's why taking care of it isn't optional — it's ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("essential.")
                    }
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    NeuroViewExamTheme {
        DashboardScreen()
    }
}