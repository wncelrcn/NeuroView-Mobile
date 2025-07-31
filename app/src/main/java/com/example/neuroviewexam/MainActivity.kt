package com.example.neuroviewexam

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.neuroviewexam.ui.theme.NeuroViewExamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroViewExamTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.home),
                contentScale = ContentScale.Crop
            )
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_logo),
            contentDescription = "Home Logo",
            modifier = Modifier
                .size(360.dp)
                .align(Alignment.Center)
                .offset(y = (-60).dp)
        )

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(
                text = "Welcome to",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "NeuroView",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Smart MRI analysis. Fast results. Anywhere you need it.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = {
                    val intent = Intent(context, Class.forName("com.example.neuroviewexam.activities.DashboardActivity"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    NeuroViewExamTheme {
        HomeScreen()
    }
}