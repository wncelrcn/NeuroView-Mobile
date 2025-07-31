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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.neuroviewexam.components.BottomNavigationBar
import com.example.neuroviewexam.components.TopAppBar
import com.example.neuroviewexam.network.ApiService
import com.example.neuroviewexam.network.ImageData
import com.example.neuroviewexam.ui.theme.NeuroViewExamTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class PastRecordsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroViewExamTheme {
                PastRecordsScreen()
            }
        }
    }
}

@Composable
fun PastRecordsScreen() {
    val context = LocalContext.current
    val apiService = remember { ApiService() }
    var pastRecords by remember { mutableStateOf<List<ImageData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        launch {
            val result = apiService.getPastRecords()
            isLoading = false
            result.onSuccess { response ->
                pastRecords = response.data
                errorMessage = null
            }.onFailure { e ->
                errorMessage = "Failed to fetch records: ${e.message}"
                pastRecords = emptyList()
            }
        }
    }

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
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "COLLECTIONS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF737373),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Repository of NeuroView",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    text = "Glioma",
                    isSelected = selectedFilter == "glioma"
                ) {
                    selectedFilter = if (selectedFilter == "glioma") null else "glioma"
                }
                FilterChip(
                    text = "No Tumor",
                    isSelected = selectedFilter == "notumor"
                ) {
                    selectedFilter = if (selectedFilter == "notumor") null else "notumor"
                }
                FilterChip(
                    text = "Pituitary",
                    isSelected = selectedFilter == "pituitary"
                ) {
                    selectedFilter = if (selectedFilter == "pituitary") null else "pituitary"
                }
                FilterChip(
                    text = "Meningioma",
                    isSelected = selectedFilter == "meningioma"
                ) {
                    selectedFilter = if (selectedFilter == "meningioma") null else "meningioma"
                }
            }

            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
                Text("Loading records...", color = Color.White, modifier = Modifier.padding(top = 16.dp))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "An unknown error occurred.",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Button(onClick = {
                    isLoading = true
                    errorMessage = null
                    coroutineScope.launch {
                        val result = apiService.getPastRecords()
                        isLoading = false
                        result.onSuccess { response ->
                            pastRecords = response.data
                            errorMessage = null
                        }.onFailure { e ->
                            errorMessage = "Failed to fetch records: ${e.message}"
                            pastRecords = emptyList()
                        }
                    }
                }) {
                    Text("Retry")
                }
            } else {
                val filteredRecords = remember(pastRecords, selectedFilter) {
                    if (selectedFilter == null) {
                        pastRecords
                    } else {
                        pastRecords.filter { 
                            it.information?.tumor_type?.lowercase(Locale.getDefault()) == selectedFilter?.lowercase(Locale.getDefault()) 
                        }
                    }
                }

                if (filteredRecords.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No records found for the selected filter.",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        if (selectedFilter != null) {
                            Button(onClick = { selectedFilter = null }) {
                                Text("Show All Records")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val chunkedRecords = filteredRecords.chunked(2)
                        items(chunkedRecords) { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowItems.forEach { record ->
                                    Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                                        RecordCard(
                                            record = record,
                                            onCardClick = { clickedRecord ->

                                                clickedRecord.information?.let { predictionInfo ->
                                                    try {
                                                        val json = Json {
                                                            ignoreUnknownKeys = true
                                                            isLenient = true
                                                        }
                                                        val predictionJson = json.encodeToString(predictionInfo)
                                                        
                                                        val intent = Intent(context, ResultActivity::class.java)
                                                        intent.putExtra("prediction_json", predictionJson)
                                                        intent.putExtra("image_uri", clickedRecord.url)
                                                        context.startActivity(intent)
                                                    } catch (e: Exception) {
                                                        println("NeuroView: Failed to encode data for Intent: ${e.message}")
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }

                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color.White else Color.DarkGray,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun RecordCard(record: ImageData, onCardClick: (ImageData) -> Unit) {
    val cardSize = 180.dp
    val imageHeight = 100.dp

    Card(
        modifier = Modifier
            .width(cardSize)
            .height(cardSize)
            .clickable { onCardClick(record) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = record.url),
                    contentDescription = "Brain Scan Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }


            Text(
                text = record.information?.tumor_type?.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
                } ?: "N/A",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = record.information?.confidence?.let { "%.2f".format(it * 100) + "%" } ?: "N/A",
                color = Color.LightGray,
                fontSize = 12.sp
            )


            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val date = try {
                record.uploaded_at?.let { inputFormat.parse(it) }
            } catch (_: Exception) {
                null
            }
            val formattedDate = date?.let { outputFormat.format(it) } ?: "N/A"

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
                Text(
                    text = "View >",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PastRecordsScreenPreview() {
    NeuroViewExamTheme {
        PastRecordsScreen()
    }
}