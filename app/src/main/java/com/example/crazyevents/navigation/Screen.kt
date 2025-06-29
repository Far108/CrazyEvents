package com.example.crazyevents.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object HomeScreen : Screen("home_screen")
    object ExploreScreen : Screen("explore_screen")
    object FollowScreen : Screen("follow_screen")
    object BlogScreen : Screen("blog_screen")
    object ProfileScreen : Screen("profile_screen")
    object EventView : Screen("eventView/{eventId}") {
        fun createRoute(eventId: String): String = "eventView/$eventId"
    }
}
