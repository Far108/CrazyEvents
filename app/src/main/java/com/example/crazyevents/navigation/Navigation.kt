package com.example.crazyevents.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crazyevents.login.LoginRegisterScreen
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation(
    modifier: Modifier
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(
            route = Screen.LoginScreen.route,
        ) {
            LoginRegisterScreen(
                navHostController = navController,
                //modifier = modifier
            )
        }


        }
    }
