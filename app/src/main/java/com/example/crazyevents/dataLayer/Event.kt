package com.example.crazyevents.dataLayer

data class Event(
    val title: String,
    val description: String,
    val creator: String,
    val date: String,
    val category: String,
    val imageUrl: String? = null
)