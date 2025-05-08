package com.example.crazyevents.dataLayer

import com.example.crazyevents.R

data class Event(
    val title: String,
    val description: String,
    val location: String,
    val address: String,
    val creator: String,
    val date: String,
    val category: String,
    val mainImageUrl: Int = R.drawable.logo,
    val going: Int? = 0
)

fun getDummyEvents(): List<Event>{
    return listOf(
        Event(
            title       = "Summer Festival",
            description = "Ein buntes Open-Air-Konzert im Stadtpark",
            location = "Stadtpark",
            address = "iwo",
            creator     = "John Doe",
            date        = "2025-06-21 18:00",
            category    = "Music"
        ),
        Event(
            title       = "Art Exhibition",
            description = "Zeitgenössische Kunstwerke ausstellen",
            creator     = "Art Gallery",
            date        = "2025-07-10 10:00",
            category    = "Art",
            location = "Stadtpark",
            address = "iwo",
        ),
        Event(
            title       = "Tech Meetup",
            description = "Neueste Tech-Trends diskutieren",
            creator     = "Tech Community",
            date        = "2025-05-20 18:30",
            category    = "Tech",
            location = "Stadtpark",
            address = "iwo",
        ),
        Event(
            title       = "Cooking Class",
            description = "Italienische Küche selbst zubereiten",
            creator     = "Chef Anna",
            date        = "2025-05-15 14:00",
            category    = "Cooking",
            location = "Stadtpark",
            address = "iwo",
        ),
        Event(
            title       = "City Marathon",
            description = "Stadtmarathon für Hobby- und Profi-Läufer",
            creator     = "Sports Club",
            date        = "2025-09-01 09:00",
            category    = "Sports",
            location = "Stadtpark",
            address = "iwo",
        )
    )
}