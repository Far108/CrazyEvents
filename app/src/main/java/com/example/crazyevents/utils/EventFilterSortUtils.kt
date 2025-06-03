package com.example.crazyevents.utils

import com.example.crazyevents.data.Event
import com.example.crazyevents.presentation.SortOption
import java.time.LocalDate

fun filterAndSortEvents(
    events: List<Event>,
    category: String?,
    location: String,
    date: LocalDate?,
    sort: SortOption
): List<Event> {
    var filtered = events

    if (!category.isNullOrBlank()) {
        filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
    }

    val locationQuery = location.trim().lowercase()

    if (location.isNotBlank()) {
        filtered = filtered.filter {
            it.address.lowercase().contains(locationQuery) ||
                    it.location.lowercase().contains(locationQuery)
        }
    }

    // Filter: Datum (nur yyyy-MM-dd beachten, Uhrzeit ignorieren)
    if (date != null) {
        filtered = filtered.filter {
            try {
                val eventDate = LocalDate.parse(it.date.substring(0, 10)) // ISO-Format: yyyy-MM-dd
                eventDate == date
            } catch (e: Exception) {
                false // Falls ungültiges Datum → überspringen
            }
        }
    }

    return when (sort) {
        SortOption.DATE_ASC -> filtered.sortedBy { it.date }
        SortOption.DATE_DESC -> filtered.sortedByDescending { it.date }
        SortOption.TITLE -> filtered.sortedBy { it.title }
        SortOption.LOCATION -> filtered.sortedBy { it.address }
        SortOption.GOING -> filtered.sortedByDescending { it.goingUserIds.size }
        else -> filtered
    }
}
