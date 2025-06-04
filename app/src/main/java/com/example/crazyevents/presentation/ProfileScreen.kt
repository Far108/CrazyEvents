package com.example.crazyevents.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crazyevents.R
import com.example.crazyevents.data.UserProfile
import com.example.crazyevents.data.Event
import com.example.crazyevents.login.UserSession
import com.example.crazyevents.model.ProfileViewModel
import com.example.crazyevents.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val yourEvents by viewModel.yourEvents.collectAsState()
    val acceptedEvents by viewModel.acceptedEvents.collectAsState()

    val currentUser = UserSession.currentUser
    val userName = currentUser?.name ?: "Unbekannt"

    val upcomingEvents = acceptedEvents.filter { !isPast(it.date) }
    val oldEvents = acceptedEvents.filter { isPast(it.date) }
    val upcoming by viewModel.upcomingNotifications.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchEvents()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (upcoming.isNotEmpty()) {
                TopAppBar(title = {
                    Text(
                        "🔔 Du hast bald ein Event: ${upcoming[0].title}",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(12.dp)
                    )
                })
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Profil", style = MaterialTheme.typography.headlineMedium)
                    TextButton(onClick = {
                        navHostController.navigate("settings")
                    }) {
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
                        painter = painterResource(R.drawable.user),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(userName, style = MaterialTheme.typography.titleLarge)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Deine Events
            item {
                SectionWithTitle(
                    navHostController,
                    title = "Deine Events",
                    events = yourEvents,
                    showAddButton = true,
                    onAddClick = { navHostController.navigate(Screen.BlogScreen.route) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Zugesagt
            item {
                SectionWithTitle(
                    navHostController,
                    title = "Zugesagt",
                    events = upcomingEvents
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Old Events
            item {
                Text("Old Events", style = MaterialTheme.typography.titleMedium)
                EventList(navHostController = navHostController, events = oldEvents)
            }
        }
    }
}

    //ChatGPT: Check Date
    fun toEpochMs(dateString: String): Long {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            val date = sdf.parse(dateString)
            date?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    fun isPast(dateString: String): Boolean {
        return toEpochMs(dateString) < System.currentTimeMillis()
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
                    Icon(Icons.Default.AddCircle, contentDescription = "Add Event")
                }
            }
        }
        EventList(navHostController = navHostController, events = events)
    }


@Composable
fun EventList(navHostController: NavHostController, events: List<Event>) {
    Column {
        events.forEach { event ->
            EventCard(event = event) { navHostController.navigate(Screen.EventView.createRoute(event.id)) }
        }
    }
}
