package com.example.crazyevents.presentation


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crazyevents.data.Event
import com.example.crazyevents.data.Poster
import com.example.crazyevents.model.ExploreViewModel


@Composable
fun ExploreScreen(viewModel: ExploreViewModel = viewModel()) {
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchExploreEvents() // Trigger only once when Composable is first shown
    }

    when {
        isLoading -> CircularProgressIndicator()
        error != null -> Text("Error: $error")
        events.isEmpty() -> Text("No posters found.")
        else -> {
            Text(text = "SearchPage")
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(events) { event ->
                    EventCard(viewModel, event)
                }
            }
        }
    }
}


@Composable
fun EventCard(viewModel: ExploreViewModel, event: Event) {
    val isInterestedInitially = remember(event.id) {
        // Replace this with actual logic:
        // e.g., viewModel.isCurrentUserInterestedIn(event.id)
        false // Default placeholder
    }
    var checked by remember(event.id) { mutableStateOf(isInterestedInitially) }

    // If the event data in the list can change (e.g., due to a refresh from the ViewModel),
    // you might want a LaunchedEffect to update 'checked' if 'isInterestedInitially' changes.
    LaunchedEffect(isInterestedInitially) {
        checked = isInterestedInitially
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Original padding
    ) {
        Row(
            modifier = Modifier.padding(8.dp), // Original padding
            verticalAlignment = Alignment.CenterVertically // Good practice
        ) {
            // Placeholder for an image, or use AsyncImage if event has an image URL
            // Text(text = "Bild") // Original
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) { // Allow text to take available space
                Text(event.title, style = MaterialTheme.typography.titleMedium) // Was event.name
                Text(event.description, style = MaterialTheme.typography.bodySmall, maxLines = 2) // Limit lines
                // In PosterCard, it was poster.category. Now it's event.category.
                Text("Category: ${event.category}", style = MaterialTheme.typography.bodySmall)
            }
            // Removed Spacer(modifier = Modifier.weight(1f)) as Column already uses weight

            Column(horizontalAlignment = Alignment.CenterHorizontally) { // Align checkbox and text
                // Text was "Follows" / "Unfollows" based on `poster.isFollowed`
                // Now based on `checked` state for the event.
                Text(
                    text = if (checked) "Interested" else "Mark Interest",
                    style = MaterialTheme.typography.bodySmall
                )
                Checkbox(
                    checked = checked,
                    onCheckedChange = { newCheckedState ->
                        checked = newCheckedState
                        // Call ViewModel to update the interest/follow status for this event.
                        // The original called viewModel.updateFollowStatus(poster.id)
                        // So, we'll call something similar for the event.
                        viewModel.updateFollowStatus(event.id)
                        /*
                        Toast.makeText(
                            context,
                            if (newCheckedState) "Marked interest in ${event.title}" else "Removed interest from ${event.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                         */
                    },
                )
            }
        }
    }
}
