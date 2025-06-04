package com.example.crazyevents.data

import com.google.gson.annotations.SerializedName

data class Poster(
//    @SerializedName("_id")
//    val id: String,
//    val name: String,

    @SerializedName("_id")
    val id: String,

    val name: String,
    val email: String,
    val password: String,
    val follow: Boolean,

    @SerializedName("__v")
    val v: Int
    /*
    val description: String,
    val imageUrl: String,
    val category: String,
    val isFollowed: Boolean
     */
)
