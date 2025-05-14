package com.example.crazyevents.data

import com.example.crazyevents.R

val dummyUser = UserProfile(
    id = "01",
    name = "Max Mustermann"
)

val dummyYourEvents = listOf(
    Event(
        id = "11",
        title = "Kochabend mit Freunden",
        description = "Gemeinsam kochen und essen",
        location = "Max' Wohnung",
        address = "Beispielstraße 1, Berlin",
        creator = "Max Mustermann",
        date = "2025-06-01",
        category = "Kochen",
        mainImageUrl = R.drawable.logo,
        going = 4
    ),
    Event(
        id = "12",
        title = "Tech Meetup Berlin",
        description = "Vorträge und Networking",
        location = "Startup Hub",
        address = "Torstraße 100, Berlin",
        creator = "Tech Community",
        date = "2025-06-10",
        category = "Technologie",
        mainImageUrl = R.drawable.logo,
        going = 18
    )
)

val dummyAcceptedEvents = listOf(
    Event(
        id = "13",
        title = "Sommerfest 2025",
        description = "Grillen im Park mit Live-Musik",
        location = "Volkspark Friedrichshain",
        address = "Am Friedrichshain 1, Berlin",
        creator = "Stadt Berlin",
        date = "2025-07-15",
        category = "Festival",
        mainImageUrl = R.drawable.logo,
        going = 120
    ),
    Event(
        id = "14",
        title = "Open-Air Kino",
        description = "Filmnacht unter freiem Himmel",
        location = "Tempelhofer Feld",
        address = "Tempelhofer Damm 45, Berlin",
        creator = "OpenCinema Org",
        date = "2025-06-22",
        category = "Kino",
        mainImageUrl = R.drawable.logo,
        going = 60
    )
)

val dummyOldEvents = listOf(
    Event(
        id = "15",
        title = "Winterwanderung",
        description = "Wanderung durch den verschneiten Wald",
        location = "Harz",
        address = "Waldweg 12, Wernigerode",
        creator = "Wanderverein",
        date = "2025-01-12",
        category = "Natur",
        mainImageUrl = R.drawable.logo,
        going = 30
    ),
    Event(
        id = "16",
        title = "Neujahrsbrunch",
        description = "Start ins neue Jahr mit leckerem Essen",
        location = "Café Morgenrot",
        address = "Sonnenallee 67, Berlin",
        creator = "Brunch Crew",
        date = "2025-01-01",
        category = "Essen",
        mainImageUrl = R.drawable.logo,
        going = 10
    )
)
