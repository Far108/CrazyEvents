package com.example.crazyevents.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.data.Poster
import com.example.crazyevents.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PosterViewModel : ViewModel() {

    private val _posters = MutableStateFlow<List<Poster>>(emptyList())
    val posters: StateFlow<List<Poster>> = _posters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _reloadEventsTrigger = MutableStateFlow(true)
    val reloadEventsTrigger: StateFlow<Boolean> = _reloadEventsTrigger
    fun triggerReload() {
        _reloadEventsTrigger.value = true
    }

    private val _following = MutableStateFlow(true)
    val following: StateFlow<Boolean> = _following

    fun resetReloadTrigger() {
        _reloadEventsTrigger.value = false
    }

    fun updateFollowStatus(follower: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val token = TokenManager.getToken(context) ?: return@launch
            val userId = TokenManager.getUserIdFromToken(token) ?: return@launch

            try {
                val response = BackendApi.api.toggleFollow(follower, userId)
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

    fun fetchPosters(context: Context) {
        viewModelScope.launch {
            Log.d("USERS", "Fetching posters...")
            _isLoading.value = true
            _error.value = null
            val token = TokenManager.getToken(context) ?: return@launch
            val id = TokenManager.getUserIdFromToken(token) ?: return@launch

            try {
                val response = BackendApi.api.getPosters(id)
                Log.d("USERS", response.body().toString())
                if (response.isSuccessful) {
                    _posters.value = response.body() ?: emptyList()
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

    suspend fun isFollowing(follower: String, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = TokenManager.getToken(context) ?: return@withContext false
                val userId = TokenManager.getUserIdFromToken(token) ?: return@withContext false

                val response = BackendApi.api.isFollowing(follower, userId)
                if (response.isSuccessful) {
                    val updatedPoster = response.body()
                    updatedPoster?.follow ?: false
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

}
