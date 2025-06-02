package com.example.crazyevents.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.model.AuthRequest
import com.example.crazyevents.utils.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    fun clearStatus() {
        _statusMessage.value = null
    }

    fun login(email: String, password: String, context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _statusMessage.value = null

            try {
                val response = BackendApi.api.login(AuthRequest(email = email, password = password))
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    val user = response.body()?.user
                    if (token != null && user != null) {
                        TokenManager.saveToken(context, token)
                        UserSession.currentUser = user
                        _statusMessage.value = "✅ Login erfolgreich"
                        delay(1000)
                        onSuccess()
                    } else {
                        _statusMessage.value = "❌ Ungültige Serverantwort"
                    }
                } else {
                    _statusMessage.value = "❌ Login fehlgeschlagen: ${response.code()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "❌ Netzwerkfehler: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _statusMessage.value = null

            try {
                val response = BackendApi.api.register(AuthRequest(name = name, email = email, password = password))
                _statusMessage.value = if (response.isSuccessful) {
                    "✅ Registrierung erfolgreich – jetzt einloggen"
                } else {
                    "❌ Registrierung fehlgeschlagen: ${response.code()}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "❌ Netzwerkfehler: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
