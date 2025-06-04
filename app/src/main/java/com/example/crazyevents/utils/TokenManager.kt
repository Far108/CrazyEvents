package com.example.crazyevents.utils

import android.content.Context
import androidx.core.content.edit
import org.json.JSONObject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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

    @OptIn(ExperimentalEncodingApi::class)
    fun getUserIdFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = parts[1]
            val decodedBytes = android.util.Base64.decode(payload, android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP)
            val decodedPayload = String(decodedBytes, Charsets.UTF_8)
            val json = JSONObject(decodedPayload)
            json.getString("userId")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}