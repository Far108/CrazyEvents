package com.example.crazyevents.presentation


import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.crazyevents.navigation.Screen

@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val items = listOf(
        Screen.MainScreen to Pair(Icons.Default.Home,    "Home"),
        Screen.BlogScreen to Pair(Icons.Default.Home,    "Blog"),   //TODO
        Screen.ExploreScreen to Pair(Icons.Default.Search, "Entdecken"),
        Screen.ProfileScreen to Pair(Icons.Default.Person,"Profil")
    )

    NavigationBar(modifier = modifier) {
        val navBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStack?.destination?.route

        items.forEach { (screen, pair) ->
            NavigationBarItem(
                icon = { Icon(pair.first, contentDescription = pair.second) },
                label = { Text(pair.second) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {

                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                    }
                }
            )
        }
    }
}
