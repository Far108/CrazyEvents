package com.example.crazyevents.model

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.data.Event
import com.example.crazyevents.presentation.SortOption
import com.example.crazyevents.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


class ExploreViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _interestedEvents = MutableStateFlow<Set<String>>(emptySet())
    val interestedEvents: StateFlow<Set<String>> = _interestedEvents.asStateFlow()

    private var allEventsBackup: List<Event> = emptyList()


    fun updateEventInterest(eventId: String, context: Context) {
        viewModelScope.launch {
            val token = TokenManager.getToken(context) ?: return@launch
            val userId = TokenManager.getUserIdFromToken(token) ?: return@launch

            try {
                val response = BackendApi.api.toggleEventInterest(eventId, "Bearer $token")
                if (response.isSuccessful) {
                    val goingTo = response.body()?.goingTo ?: emptyList()

                    _interestedEvents.update {
                        if (goingTo.contains(userId)) it + eventId else it - eventId
                    }
                    print(interestedEvents)
                }
            } catch (e: Exception) {
                Log.e("EventCard", "Fehler: ${e.message}")
            }
        }
    }

    fun loadInterestedEvents(context: Context) {
        viewModelScope.launch {
            val token = TokenManager.getToken(context) ?: return@launch
            val userId = TokenManager.getUserIdFromToken(token) ?: return@launch

            val allEvents = BackendApi.api.getEvents().body() ?: return@launch
            val userEventIds = allEvents
                .filter { it.goingUserIds.contains(userId) }
                .map { it.id }

            _interestedEvents.value = userEventIds.toSet()
        }
    }

    fun fetchExploreEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = BackendApi.api.getEvents()
                if (response.isSuccessful) {
                    val events = response.body() ?: emptyList()
                    _events.value = events
                    allEventsBackup = events
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
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
        var filtered = _events.value

        if (!category.isNullOrBlank()) {
            filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
        }

        val locationQuery = location.trim().lowercase()

        if (location.isNotBlank()) {
            filtered = filtered.filter {
                it.address.lowercase().contains(locationQuery) ||
                        it.location.lowercase().contains(locationQuery)
            }
        }


        if (date != null) {
            filtered = filtered.filter {
                val eventDate = LocalDate.parse(it.date.substring(0, 10))
                eventDate == date
            }
        }

        _events.value = when (sort) {
            SortOption.DATE_ASC -> filtered.sortedBy { it.date }
            SortOption.DATE_DESC -> filtered.sortedByDescending { it.date }
            SortOption.TITLE -> filtered.sortedBy { it.title }
            SortOption.LOCATION -> filtered.sortedBy { it.address }
            SortOption.GOING -> filtered.sortedByDescending { it.goingUserIds.size }
            else -> filtered
        }
    }

    fun resetFilters() {
        _events.value = allEventsBackup
    }


}
