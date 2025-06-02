package com.example.crazyevents.ui.theme

import android.content.Context
import androidx.core.content.edit

object ThemeManager {
    private const val PREF_NAME = "user_preferences"
    private const val KEY_THEME_MODE = "theme_mode" // light, dark, system

    fun saveThemeMode(context: Context, mode: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_THEME_MODE, mode)
            }
    }

    fun getThemeMode(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_THEME_MODE, "system") ?: "system"
    }
}