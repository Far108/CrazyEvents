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
}
