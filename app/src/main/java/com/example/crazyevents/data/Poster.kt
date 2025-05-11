package com.example.crazyevents.data

data class Poster(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val isFollowed: Boolean
)
