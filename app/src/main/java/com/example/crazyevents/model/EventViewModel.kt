package com.example.crazyevents.model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.data.Event
import com.example.crazyevents.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class EventViewModel: ViewModel() {

    private val _event = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _event

    fun uploadEventImages(eventId: String, uris: List<Uri>, context: Context) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val token = TokenManager.getToken(context) ?: return@launch

            val parts = uris.mapNotNull { uri ->
                val inputStream = contentResolver.openInputStream(uri) ?: return@mapNotNull null
                val bytes = inputStream.readBytes()
                val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(
                    "images", "image.jpg", requestBody
                )
            }

            try {
                val response = BackendApi.api.uploadEventImages(
                    token = "Bearer $token",
                    eventId = eventId,
                    images = parts
                )

                if (response.isSuccessful) {
                    val updatedEvent = response.body()
                    // Option: State aktualisieren oder Snackbar
                    Log.d("UPLOAD", "Upload erfolgreich")
                } else {
                    Log.e("UPLOAD", "Fehler: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD", "Netzwerkfehler: ${e.message}")
            }
        }
    }

    fun uploadGalleryImages(eventId: String, uris: List<Uri>, context: Context) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val token = TokenManager.getToken(context) ?: return@launch

            val parts = uris.mapNotNull { uri ->
                val inputStream = contentResolver.openInputStream(uri) ?: return@mapNotNull null
                val bytes = inputStream.readBytes()
                val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(
                    "images", "image.jpg", requestBody
                )
            }

            try {
                val response = BackendApi.api.uploadGalleryImages(
                    token = "Bearer $token",
                    eventId = eventId,
                    images = parts
                )

                if (response.isSuccessful) {
                    val updatedEvent = response.body()
                    Log.d("UPLOAD", "Galerie-Bilder erfolgreich hochgeladen")
                } else {
                    Log.e("UPLOAD", "Fehler: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD", "Netzwerkfehler: ${e.message}")
            }
        }
    }


    fun getEventById(id: String) {
        viewModelScope.launch {
            try {
                val response = BackendApi.api.getEventById(id)
                if (response.isSuccessful) {
                    _event.value = response.body()
                } else {
                    Log.e("EventViewModel", "Fehler: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("EventViewModel", "Netzwerkfehler: ${e.message}")
            }
        }
    }
}