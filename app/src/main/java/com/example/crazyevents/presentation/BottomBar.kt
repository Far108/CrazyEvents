package com.example.crazyevents.presentation


import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.crazyevents.data.Event
import com.example.crazyevents.model.MainScreenViewModel
import com.example.crazyevents.navigation.Screen

@Composable
fun BottomBar(viewModel: MainScreenViewModel = viewModel(), navController: NavHostController, modifier: Modifier = Modifier) {
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
                onClick = {                                             //fix from chatGPT
                    if (screen == Screen.MainScreen) {
                        val popped = navController.popBackStack(Screen.MainScreen.route, false)
                        if (!popped) {
                            // This is what you're missing: navigate to home explicitly if it's not in the back stack
                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo(Screen.LoginScreen.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                        viewModel.triggerReload()
                    } else {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }



            )
        }
    }
}


@Preview(name = "BottomBar Preview")
@Composable
private fun PreviewBottombar() {
    Text("Keine Vorschau für BottomBar", modifier = Modifier.background(Color.White))
}
