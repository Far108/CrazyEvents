package com.example.crazyevents.utils

import android.content.Context
import androidx.core.content.edit

object TokenManager {
    fun saveToken(context: Context, token: String) {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit {
                putString("jwt", token)
            }
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getString("jwt", null)
    }

    fun clearToken(context: Context) {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit {
                remove("jwt")
            }
    }
}