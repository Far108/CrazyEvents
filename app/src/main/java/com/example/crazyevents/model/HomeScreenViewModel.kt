package com.example.crazyevents.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.data.Event
import com.example.crazyevents.presentation.SortOption
import com.example.crazyevents.utils.TokenManager
import com.example.crazyevents.utils.filterAndSortEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeScreenViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent
    private val _reloadEventsTrigger = MutableStateFlow(true)
    val reloadEventsTrigger: StateFlow<Boolean> = _reloadEventsTrigger

    // Stateholder addEvents
    private val _submitMessage = MutableStateFlow<String?>(null)
    val submitMessage: StateFlow<String?> = _submitMessage.asStateFlow()

    private var allFollowedEventsBackup: List<Event> = emptyList()

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
                    val events = response.body() ?: emptyList()
                    _events.value = events
                    allFollowedEventsBackup = events
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

    fun fetchMyEvents(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val token = TokenManager.getToken(context) ?: return@launch
            val id = TokenManager.getUserIdFromToken(token) ?: return@launch

            try {
                val response = BackendApi.api.getMyEvents(id) // Call API data method
                if (response.isSuccessful) {
                    val events = response.body() ?: emptyList<Event>()

                    _events.value = events.sortedBy { it.date }
                    allFollowedEventsBackup = events.sortedBy { it.date }
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

    fun addEvent(event: Event) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _submitMessage.value = null

            try {
                val response = BackendApi.api.createEvent(event)
                if (response.isSuccessful) {
                    _events.value = _events.value + (response.body() ?: event)
                    _submitMessage.value = "✅ Event wurde erfolgreich gespeichert!"
                } else {
                    _submitMessage.value = "❌ Fehler: ${response.code()} – ${response.message()}"
                }
            } catch (e: Exception) {
                _submitMessage.value = "❌ Netzwerkfehler: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun applyFiltersAndSort(
        category: String?,
        location: String,
        date: LocalDate?,
        sort: SortOption
    ) {

        _events.value = filterAndSortEvents(allFollowedEventsBackup, category, location, date, sort)
    }

}