package com.example.crazyevents.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.data.Event
import com.example.crazyevents.data.Poster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ExploreViewModel : ViewModel() {

    private val _posters = MutableStateFlow<List<Poster>>(emptyList())
    val posters: StateFlow<List<Poster>> = _posters

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun updateFollowStatus(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = BackendApi.api.toggleFollow(userId)
                if (response.isSuccessful) {
                    //_posters.value = response.body() ?: ""
                    val updatedPoster = response.body()

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

    fun fetchExploreEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = BackendApi.api.getEvents()
                if (response.isSuccessful) {
                    _events.value = response.body() ?: emptyList()
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
}
