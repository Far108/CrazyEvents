package com.example.crazyevents.login

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crazyevents.api.BackendApi
import com.example.crazyevents.model.AuthRequest
import com.example.crazyevents.navigation.Screen
import com.example.crazyevents.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginRegisterScreen(
    navHostController: NavHostController,
    context: Context = LocalContext.current
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Willkommen", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name (nur bei Registrierung)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-Mail") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Passwort") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(text = it, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (loading) {
            CircularProgressIndicator()
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    loading = true
                    errorMessage = null
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = BackendApi.api.login(AuthRequest(email = email, password = password))
                            withContext(Dispatchers.Main) {
                                loading = false
                                if (response.isSuccessful) {
                                    val token = response.body()?.token
                                    val user = response.body()?.user
                                    if (token != null) {
                                        TokenManager.saveToken(context, token)
                                        navHostController.navigate(Screen.MainScreen.route)
                                        UserSession.currentUser = user
                                    } else {
                                        errorMessage = "Ungültige Antwort vom Server"
                                    }
                                } else {
                                    errorMessage = "Login fehlgeschlagen: ${response.code()}"
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                loading = false
                                errorMessage = "Netzwerkfehler: ${e.message}"
                            }
                        }
                    }
                }) {
                    Text("Login")
                }

                Button(onClick = {
                    loading = true
                    errorMessage = null
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = BackendApi.api.register(AuthRequest(name = name, email = email, password = password))
                            withContext(Dispatchers.Main) {
                                loading = false
                                errorMessage = if (response.isSuccessful) {
                                    "Registrierung erfolgreich! Jetzt einloggen."
                                } else {
                                    "Registrierung fehlgeschlagen: ${response.code()}"
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                loading = false
                                errorMessage = "Netzwerkfehler: ${e.message}"
                            }
                        }
                    }
                }) {
                    Text("Registrieren")
                }
            }
        }
    }
}

