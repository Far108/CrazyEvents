package com.example.crazyevents.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.api.BackendApiService
import com.example.crazyevents.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchEvents()
    }

    fun fetchEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = BackendApi.api.getEvents() // Call API data method
                if (response.isSuccessful) {
                    _events.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                    print("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Network Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}