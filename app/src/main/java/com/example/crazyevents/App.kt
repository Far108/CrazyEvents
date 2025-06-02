package com.example.crazyevents

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.crazyevents.navigation.Navigation
import com.example.crazyevents.ui.theme.CrazyEventsTheme
import com.example.crazyevents.ui.theme.ThemeManager

@Composable
fun CrazyEventsApp() {
    val context = LocalContext.current

    // Theme-Zustand zentral speichern
    var themeMode by rememberSaveable {
        mutableStateOf(ThemeManager.getThemeMode(context))
    }

    val isDarkTheme = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    CrazyEventsTheme(darkTheme = isDarkTheme) {
        Navigation(
            modifier = Modifier.fillMaxSize(),
            currentTheme = themeMode,
            onThemeChange = { newTheme ->
                themeMode = newTheme
                ThemeManager.saveThemeMode(context, newTheme)
            }
        )
    }
}



