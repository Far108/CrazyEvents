package com.example.crazyevents.model

import com.example.crazyevents.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.login.UserSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _allEvents = MutableStateFlow<List<Event>>(emptyList())
    val allEvents: StateFlow<List<Event>> = _allEvents

    init {
        fetchEvents()
    }

    fun fetchEvents() {
        viewModelScope.launch {
            val response = BackendApi.api.getEvents()
            if (response.isSuccessful) {
                _allEvents.value = response.body() ?: emptyList()
            }
        }
    }

    val yourEvents = allEvents.map { list ->
        list.filter { it.creator?.id == UserSession.currentUser?.id }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val acceptedEvents = allEvents.map { list ->
        list.filter { UserSession.currentUser?.id in it.goingUserIds }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun daysUntilEvent(event: Event): Long? {
        return try {
            val eventDate = java.time.LocalDate.parse(event.date.substring(0, 10))
            val today = java.time.LocalDate.now()
            java.time.temporal.ChronoUnit.DAYS.between(today, eventDate)
        } catch (e: Exception) {
            null
        }
    }

    val upcomingEvents1Day = acceptedEvents.map { list ->
        list.filter { daysUntilEvent(it) == 1L }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val upcomingEvents3Days = acceptedEvents.map { list ->
        list.filter { daysUntilEvent(it) == 3L }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val upcomingEvents7Days = acceptedEvents.map { list ->
        list.filter { daysUntilEvent(it) == 7L }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val upcomingNotifications = acceptedEvents.map { list ->
        list.filter {
            val days = daysUntilEvent(it)
            days == 1L || days == 3L || days == 7L
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())





}
