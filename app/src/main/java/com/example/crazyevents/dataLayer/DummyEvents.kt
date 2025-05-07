package com.example.crazyevents.dataLayer

class DummyEvents: List<Event> by listOf(Event(
    title       = "Summer Festival",
    description = "Ein buntes Open-Air-Konzert im Stadtpark",
    creator     = "John Doe",
    date        = "2025-06-21 18:00",
    category    = "Music",
    imageUrl    = "https://example.com/summer_festival.jpg"
),
    Event(
        title       = "Art Exhibition",
        description = "Zeitgenössische Kunstwerke ausstellen",
        creator     = "Art Gallery",
        date        = "2025-07-10 10:00",
        category    = "Art",
        imageUrl    = "https://example.com/art_exhibition.jpg"
    ),
    Event(
        title       = "Tech Meetup",
        description = "Neueste Tech-Trends diskutieren",
        creator     = "Tech Community",
        date        = "2025-05-20 18:30",
        category    = "Tech",
        imageUrl    = "https://example.com/tech_meetup.jpg"
    ),
    Event(
        title       = "Cooking Class",
        description = "Italienische Küche selbst zubereiten",
        creator     = "Chef Anna",
        date        = "2025-05-15 14:00",
        category    = "Cooking",
        imageUrl    = "https://example.com/cooking_class.jpg"
    ),
    Event(
        title       = "City Marathon",
        description = "Stadtmarathon für Hobby- und Profi-Läufer",
        creator     = "Sports Club",
        date        = "2025-09-01 09:00",
        category    = "Sports",
        imageUrl    = null
    )
)