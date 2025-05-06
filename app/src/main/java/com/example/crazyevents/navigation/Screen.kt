package com.example.crazyevents.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen(route = "login_screen")
    {

    }
}