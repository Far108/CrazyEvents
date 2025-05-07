package com.example.crazyevents.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crazyevents.navigation.Screen

@Composable
fun LoginRegisterScreen(
    navHostController: NavHostController,
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
           Button(onClick = { navHostController.navigate(Screen.MainScreen.route) }) {
             Text("Login")
            }
          //  Button(onClick = { onRegister(email, password) }) {
          //      Text("Registrieren")
            }
        }}


/*
Usage:

In deiner Activity oder deinem Fragment:

setContent {
    MaterialTheme {
        LoginRegisterScreen(
            onLogin = { email, password ->
                // Login-Logik hier
            },
            onRegister = { email, password ->
                // Registrierungs-Logik hier
            }
        )
    }
}
*/
