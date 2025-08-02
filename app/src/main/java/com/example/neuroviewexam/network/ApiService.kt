package com.example.neuroviewexam.network

import android.content.Context
import android.net.Uri
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

@Serializable
data class CreateImageRequest(
    val name: String
)

@Serializable
data class PredictionData(
    val success: Boolean,
    val predicted_class: Int? = null,
    val tumor_type: String? = null,
    val confidence: Double? = null,
    val probabilities: List<Double>? = null,
    val class_probabilities: Map<String, Double>? = null,
    val message: String? = null,
    val error: String? = null
)

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ImageData? = null,
    val prediction: PredictionData? = null,
    val prediction_error: String? = null
)

@Serializable
data class ImageData(
    val id: String,
    val name: String,
    val url: String,
    val uploaded_at: String,
    val information: PredictionData? = null
)

@Serializable
data class PastRecordsResponse(
    val count: Int,
    val data: List<ImageData>,
    val message: String,
    val success: Boolean
)


class ApiService {
    private val baseUrl = "https://neuroview-backend.onrender.com/api/auto"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun createImage(name: String): Result<ApiResponse> {
        return try {
            val response: HttpResponse = client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(CreateImageRequest(name = name))
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    Result.success(ApiResponse(success = true, message = "Image created successfully"))
                }
                else -> {
                    Result.failure(Exception("Server returned ${response.status.value}: ${response.bodyAsText()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImage(context: Context, imageUri: Uri, imageName: String): Result<ApiResponse> {
        return try {
            println("NeuroView: Starting image upload to $baseUrl")

            val inputStream = context.contentResolver.openInputStream(imageUri)
            val imageBytes = inputStream?.use { input ->
                val outputStream = ByteArrayOutputStream()
                input.copyTo(outputStream)
                outputStream.toByteArray()
            } ?: throw Exception("Could not read image file")

            println("NeuroView: Image size: ${imageBytes.size} bytes")

            val fileName = imageName.ifBlank { "image.jpg" }
            val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

            println("NeuroView: Uploading file: $fileName, type: $mimeType")

            val response: HttpResponse = client.submitFormWithBinaryData(
                url = baseUrl,
                formData = formData {
                    append("file", imageBytes, Headers.build {
                        append(HttpHeaders.ContentType, mimeType)
                        append(HttpHeaders.ContentDisposition, "form-data; name=file; filename=$fileName")
                    })
                    append("name", imageName)
                }
            )

            println("NeuroView: Response status: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val responseText = response.bodyAsText()
                    println("NeuroView: Response body: $responseText")

                    val jsonResponse = Json { ignoreUnknownKeys = true }.decodeFromString<ApiResponse>(responseText)
                    println("NeuroView: Parsed response: $jsonResponse")

                    if (jsonResponse.prediction == null) {
                        println("NeuroView: Warning - No prediction data in response")
                    } else {
                        println("NeuroView: Prediction success: ${jsonResponse.prediction?.success}")
                    }

                    Result.success(jsonResponse)
                }
                else -> {
                    val errorBody = response.bodyAsText()
                    println("NeuroView: Error response: $errorBody")
                    Result.failure(Exception("Server returned ${response.status.value}: $errorBody"))
                }
            }
        } catch (e: Exception) {
            println("NeuroView: Upload exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getPastRecords(): Result<PastRecordsResponse> {
        return try {
            println("NeuroView: Fetching past records from $baseUrl")
            val response: HttpResponse = client.get(baseUrl)

            println("NeuroView: Past Records Response status: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val responseText = response.bodyAsText()
                    println("NeuroView: Past Records Response body: $responseText")
                    val pastRecordsResponse = Json { ignoreUnknownKeys = true }.decodeFromString<PastRecordsResponse>(responseText)
                    Result.success(pastRecordsResponse)
                }
                else -> {
                    val errorBody = response.bodyAsText()
                    println("NeuroView: Past Records Error response: $errorBody")
                    Result.failure(Exception("Server returned ${response.status.value}: $errorBody"))
                }
            }
        } catch (e: Exception) {
            println("NeuroView: Fetching past records exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun close() {
        client.close()
    }
}