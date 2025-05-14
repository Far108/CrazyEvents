package com.example.crazyevents.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crazyevents.R
import com.example.crazyevents.data.UserProfile
import com.example.crazyevents.data.Event
import com.example.crazyevents.navigation.Screen

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    userProfile: UserProfile,
    onEditClick: () -> Unit,
    onAddEventClick: () -> Unit,
    yourEvents: List<Event>,
    acceptedEvents: List<Event>,
    oldEvents: List<Event>
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Profil", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = onEditClick) {
                Text("Bearbeiten")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Profile Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.logo), // use actual profile image if available
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(userProfile.name, style = MaterialTheme.typography.titleLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Deine Events
        SectionWithTitle(
            navHostController,
            title = "Deine Events",
            events = yourEvents,
            showAddButton = true,
            onAddClick = onAddEventClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Zugesagt
        SectionWithTitle(
            navHostController,
            title = "Zugesagt",
            events = acceptedEvents
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Old Events
        Text("Old Events", style = MaterialTheme.typography.titleMedium)
        EventList(navHostController= navHostController,events = oldEvents)
    }
}

@Composable
fun SectionWithTitle(
    navHostController: NavHostController,
    title: String,
    events: List<Event>,
    showAddButton: Boolean = false,
    onAddClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if (showAddButton && onAddClick != null) {
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    }
    EventList(navHostController= navHostController, events = events)
}

@Composable
fun EventList(navHostController: NavHostController, events: List<Event>) {
    Column {
        events.forEach { event ->   //TODO: doesn't show Event - not from backend
            EventCard(event = event) {navHostController.navigate(Screen.EventView.createRoute(event.id))}
            }
        }
    }
