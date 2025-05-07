package com.example.crazyevents.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.crazyevents.login.LoginRegisterScreen
import com.example.crazyevents.presentation.BlogScreen
import com.example.crazyevents.presentation.BottomBar
import com.example.crazyevents.presentation.ExploreScreen
import com.example.crazyevents.presentation.MainScreen
import com.example.crazyevents.presentation.ProfileScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            // BottomBar nur zeigen, wenn nicht im Login
            if (currentRoute != Screen.LoginScreen.route) {
                BottomBar(navController)
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
                MainScreen()
            }
            composable(Screen.ExploreScreen.route) {
                ExploreScreen()
            }
            composable(Screen.BlogScreen.route) {
                BlogScreen()
            }
            composable(Screen.ProfileScreen.route) {
                ProfileScreen()
            }
        }
    }
}
