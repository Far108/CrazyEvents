package com.example.crazyevents.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.data.UserProfile
import com.example.crazyevents.data.UserUpdateRequest
import com.example.crazyevents.login.UserSession
import com.example.crazyevents.ui.theme.ThemeSwitcher
import com.example.crazyevents.utils.SuccessMessage
import com.example.crazyevents.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen(
    navController: NavHostController,
    currentTheme: String,
    onThemeChange: (String) -> Unit
) {
    val context = LocalContext.current
    val user = UserSession.currentUser

    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Einstellungen", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        ThemeSwitcher(
            context = context,
            currentMode = currentTheme,
            onModeChanged = { selected ->
                onThemeChange(selected)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 👇 Userdaten bearbeiten
        Text("Profil bearbeiten", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-Mail") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val token = TokenManager.getToken(context) ?: return@Button
                val userId = user?.id ?: return@Button

                val request = UserUpdateRequest(name = name, email = email)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = BackendApi.api.updateUser(
                            token = "Bearer $token",
                            userId = userId,
                            updateRequest = request
                        )
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                val updatedUser = response.body()
                                UserSession.currentUser = updatedUser

                                // 🟢 aktualisiere auch lokale UI-States
                                name = updatedUser?.name ?: ""
                                email = updatedUser?.email ?: ""

                                message = "Änderung erfolgreich gespeichert"
                            } else {
                                message = "Fehler: ${response}"
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            message = "Netzwerkfehler: ${e.message}"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Speichern")
        }

        message?.let {
            Spacer(modifier = Modifier.height(8.dp))
            SuccessMessage(message = it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Zurück")
        }
    }
}

