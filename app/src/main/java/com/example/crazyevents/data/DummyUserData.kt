package com.example.crazyevents.data

import com.example.crazyevents.R

val dummyUser = UserProfile(
    id = "01",
    name = "Max Mustermann"
)
// --- Dummy Data ---

// Assume "currentUser" is the user viewing the profile screen.
// This is just for making the dummy data more logical.
val currentUserId = "currentUser123"
val otherUserId1 = "otherUser456"
val otherUserId2 = "otherUser789"

val dummyCreator1 = Creator(id = currentUserId, name = "My User Name")
val dummyCreator2 = Creator(id = otherUserId1, name = "Jane Doe")
val dummyCreator3 = Creator(id = otherUserId2, name = "Alex Smith")

// Events created by the current user
val dummyYourEvents: List<Event> = listOf(
    Event(
        id = "event101",
        title = "My Birthday Bash",
        description = "Celebrating another year! Join me for fun and cake.",
        location = "My Place",
        address = "123 Main St, Anytown",
        creator = dummyCreator1, // Event created by "me"
        date = "2024-12-15 19:00", // Future event
        category = "Party",
        goingUserIds = listOf(currentUserId, otherUserId1)
        // mainImageUrl = R.drawable.birthday_event_placeholder // Optional
    ),
    Event(
        id = "event102",
        title = "Weekend Coding Sprint",
        description = "Let's build something cool this weekend. All skill levels welcome.",
        location = "Co-working Space Zeta",
        address = "456 Tech Ave, Codeville",
        creator = dummyCreator1, // Event created by "me"
        date = "2024-11-30 09:00", // Future event
        category = "Tech",
        goingUserIds = listOf(currentUserId, otherUserId2, "someOtherUserABC")
    )
)

// Events the current user has accepted/is going to (but didn't create)
val dummyAcceptedEvents: List<Event> = listOf(
    Event(
        id = "event201",
        title = "Community Cleanup Day",
        description = "Help us make our neighborhood shine!",
        location = "Greenwood Park",
        address = "789 Park Rd, Greenville",
        creator = dummyCreator2, // Created by someone else
        date = "2024-11-23 10:00", // Future event
        category = "Community",
        goingUserIds = listOf(otherUserId1, currentUserId, otherUserId2) // "currentUser" is going
        // mainImageUrl = R.drawable.community_event_placeholder // Optional
    ),
    Event(
        id = "event202",
        title = "Indie Music Night",
        description = "Discover local bands and enjoy great music.",
        location = "The Underground Venue",
        address = "000 Basement Ln, Soundsville",
        creator = dummyCreator3, // Created by someone else
        date = "2024-12-05 20:00", // Future event
        category = "Music",
        goingUserIds = listOf(currentUserId, "friend1", "friend2", "friend3") // "currentUser" is going
    )
)

// Events that have already passed
val dummyOldEvents: List<Event> = listOf(
    Event(
        id = "event301",
        title = "Summer BBQ (Past)",
        description = "Grill & Chill - was a great time!",
        location = "Lakeview Picnic Area",
        address = "1 BBQ Point, Lakeside",
        creator = dummyCreator2,
        date = "2024-07-20 13:00", // Past event
        category = "Food",
        goingUserIds = listOf(otherUserId1, currentUserId, "anotherAttendee")
    ),
    Event(
        id = "event302",
        title = "Tech Conference 2023 (Past)",
        description = "Keynotes and workshops from last year's tech conf.",
        location = "Convention Center",
        address = "Big Hall Rd, Metropolis",
        creator = dummyCreator1, // Could be one of "my" old events
        date = "2023-10-15 09:00", // Past event
        category = "Conference",
        goingUserIds = listOf(currentUserId, otherUserId1, otherUserId2)
    ),
    Event(
        id = "event303",
        title = "Art Workshop (Past)",
        description = "Learned new painting techniques.",
        location = "Community Art Studio",
        address = "Creative Corner 1A",
        creator = dummyCreator3,
        date = "2024-01-10 14:00", // Past event
        category = "Arts",
        goingUserIds = listOf(currentUserId, otherUserId2)
    )
)

// You can also create a single function to get all dummy data if needed by a preview or test
fun getAllDummyProfileEvents(): Triple<List<Event>, List<Event>, List<Event>> {
    return Triple(dummyYourEvents, dummyAcceptedEvents, dummyOldEvents)
}
