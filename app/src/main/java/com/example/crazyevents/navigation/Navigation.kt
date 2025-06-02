package com.example.crazyevents.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.crazyevents.data.*
import com.example.crazyevents.login.LoginRegisterScreen
import com.example.crazyevents.model.MainScreenViewModel
import com.example.crazyevents.presentation.BlogScreen
import com.example.crazyevents.presentation.BottomBar
import com.example.crazyevents.presentation.Event
import com.example.crazyevents.presentation.ExploreScreen
import com.example.crazyevents.presentation.MainScreen
import com.example.crazyevents.presentation.ProfileScreen
import com.example.crazyevents.presentation.SettingsScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    currentTheme: String,
    onThemeChange: (String) -> Unit
) {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            // BottomBar nur zeigen, wenn nicht im Login
            if (currentRoute != Screen.LoginScreen.route) {     //TODO: anders lösen
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.LoginScreen.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.LoginScreen.route) {
                LoginRegisterScreen(navController)
            }
            composable(Screen.MainScreen.route) {
                MainScreen( navHostController = navController)
            }
            composable(Screen.ExploreScreen.route) {
                ExploreScreen(navHostController = navController)
            }
            composable(Screen.BlogScreen.route) {
                BlogScreen()
            }
            composable(Screen.ProfileScreen.route) {
                ProfileScreen(
                    navController,
                    onEditClick = { /* TODO */ }
                )
            }
            composable(Screen.EventView.route){
                    backStackEntry ->
                val viewModel: MainScreenViewModel = viewModel()
                val eventId = backStackEntry.arguments?.getString("eventId")

                // trigger fetch if ID exists
                if (eventId != null) {
                    LaunchedEffect(eventId) {
                        viewModel.getEventById(eventId)
                    }
                }

                val selectedEvent by viewModel.selectedEvent.collectAsState()

                if (selectedEvent != null) {
                    Event(event = selectedEvent!!)
                } else {
                    //TODO: show loading or error
                }
            }
            composable("settings") {
                SettingsScreen(
                    navController = navController,
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange
                )
            }
        }
    }
}
