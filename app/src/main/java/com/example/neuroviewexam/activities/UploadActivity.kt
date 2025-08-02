package com.example.neuroviewexam.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neuroviewexam.R
import com.example.neuroviewexam.components.TopAppBar
import com.example.neuroviewexam.network.ApiService
import com.example.neuroviewexam.ui.theme.NeuroViewExamTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UploadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuroViewExamTheme {
                UploadScreen()
            }
        }
    }
}

@Composable
fun UploadScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val apiService = remember { ApiService() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            Toast.makeText(context, "Image selected", Toast.LENGTH_SHORT).show()
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with header and content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "UPLOAD",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF737373)
                        )
                        IconButton(
                            onClick = {
                                val intent = Intent(context, DashboardActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                context.startActivity(intent)
                            },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF737373)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                selectedImageUri?.let { uri ->
                    ImageInfoBox(uri = uri, onDelete = { selectedImageUri = null })
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Responsive dashed box that takes available space
                DashedBorderBox(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Bottom button section - always visible
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    selectedImageUri?.let { uri ->
                        val fileName = run {
                            var name = "Unknown Image"
                            val cursor = context.contentResolver.query(uri, null, null, null, null)
                            cursor?.use {
                                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                if (it.moveToFirst() && nameIndex != -1) {
                                    name = it.getString(nameIndex)
                                }
                            }
                            name
                        }

                        isLoading = true
                        coroutineScope.launch {
                            try {
                                val result = apiService.uploadImage(context, uri, fileName)
                                result.fold(
                                    onSuccess = { response ->
                                        if (response.prediction != null) {
                                            try {
                                                val jsonEncoder = Json {
                                                    ignoreUnknownKeys = true
                                                    isLenient = true
                                                    coerceInputValues = true
                                                }
                                                val predictionJson = jsonEncoder.encodeToString(response.prediction)
                                                println("NeuroView: Encoded prediction JSON: $predictionJson")
                                                
                                                val imageUriString = uri.toString()
                                                println("NeuroView: Image URI: $imageUriString")
                                                

                                                val intent = Intent(context, ResultActivity::class.java)
                                                intent.putExtra("prediction_json", predictionJson)
                                                intent.putExtra("image_uri", imageUriString)
                                                context.startActivity(intent)
                                                
                                                selectedImageUri = null

                                                if (response.prediction.success) {
                                                    Toast.makeText(context, "Analysis completed successfully!", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(context, "Analysis completed with issues: ${response.prediction.error}", Toast.LENGTH_LONG).show()
                                                }
                                            } catch (e: Exception) {
                                                val imageUriString = uri.toString()
                                                val intent = Intent(context, ResultActivity::class.java)
                                                intent.putExtra("image_uri", imageUriString)
                                                context.startActivity(intent)
                                                selectedImageUri = null
                                                Toast.makeText(context, "Analysis completed, but display issues occurred", Toast.LENGTH_LONG).show()
                                            }
                                        } else {
                                            val errorMsg = response.prediction_error ?: "No prediction data received from server"
                                            val imageUriString = uri.toString()
                                            Toast.makeText(context, "Upload successful, but analysis failed: $errorMsg", Toast.LENGTH_LONG).show()
                                            val intent = Intent(context, ResultActivity::class.java)
                                            intent.putExtra("image_uri", imageUriString)
                                            context.startActivity(intent)
                                            selectedImageUri = null
                                        }
                                    },
                                    onFailure = { error ->
                                        Toast.makeText(context, "Upload failed: ${error.message}", Toast.LENGTH_LONG).show()
                                    }
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedImageUri != null && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyzing...", color = Color.Black)
                } else {
                    Text("Upload & Start Analysis", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun DashedBorderBox(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val cornerRadius = 12.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dashLength = 10.dp.toPx()
            val gapLength = 10.dp.toPx()
            drawRoundRect(
                color = Color.White,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashLength, gapLength), 0f
                    )
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cloud_upload),
                contentDescription = "Upload Icon",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Browse Files",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Supported Formats: JPEG, PNG",
                fontSize = 12.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Upload Limit: 1 image file only.",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun ImageInfoBox(uri: Uri, onDelete: () -> Unit) {
    val context = LocalContext.current

    val fileName = remember(uri) {
        var name = "Unknown"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }
        name
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(width = 2.dp, color = Color.White, shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = fileName,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UploadScreenPreview() {
    NeuroViewExamTheme {
        UploadScreen()
    }
}