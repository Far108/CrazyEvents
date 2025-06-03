package com.example.crazyevents.data

import com.example.crazyevents.R
import com.google.gson.annotations.SerializedName

// Creator data class remains the same
data class Creator(
    val id: String, // This will hold the MongoDB ObjectId as a String
    val name: String
)

data class Event(
    @SerializedName("_id")
    val id: String, // This will hold the MongoDB ObjectId for the event as a String
    val title: String,
    val description: String,
    val location: String,
    val address: String,
    val creator: Creator?,
    val date: String,
    val category: String,
    val websiteUrl: String? = null,
    @SerializedName("goingTo")
    val goingUserIds: List<String>, // Each string in this list will be a MongoDB ObjectId
    // ... other fields like a local mainImageUrl if needed ...
) {
    val going: Int
        get() = goingUserIds.size
}