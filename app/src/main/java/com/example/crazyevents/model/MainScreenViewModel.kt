package com.example.crazyevents.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent
    private val _reloadEventsTrigger = MutableStateFlow(false)
    val reloadEventsTrigger: StateFlow<Boolean> = _reloadEventsTrigger

    init {
        fetchEvents()
    }

    fun insertEvents(eventList: List<Event>) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _events.value = eventList
            _isLoading.value = false
        }
    }


    fun getEventById(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = BackendApi.api.getEventbyId(eventId)
                if (response.isSuccessful) {
                    _selectedEvent.value = response.body()
                } else {
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                    _selectedEvent.value = null
                }
            } catch (e: Exception) {
                _error.value = "Network Error: ${e.message}"
                _selectedEvent.value = null
            } finally {
                _isLoading.value = false
            }
        }
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

    fun triggerReload() {
        _reloadEventsTrigger.value = true
    }


    fun resetReloadTrigger() {
        _reloadEventsTrigger.value = false
    }
}