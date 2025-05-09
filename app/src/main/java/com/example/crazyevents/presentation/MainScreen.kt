package com.example.crazyevents.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crazyevents.R
import com.example.crazyevents.data.Event
import com.example.crazyevents.model.MainScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel(), // Use the MainViewModel
    // Assuming filterButton and SortMenu are defined elsewhere
) {
    // Collect the state flows from the ViewModel
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // State for sorting (kept as in your original code)
    var currentSort by remember { mutableStateOf("Date") } // Default sort

    // Perform local sorting based on currentSort (kept as in your original code)
    val sortedEvents = remember(events, currentSort) {
        when (currentSort) {
            "Date" -> events.sortedBy { it.date } // Assuming date can be sorted as a string
            "Participants" -> events.sortedByDescending { it.going }
            else -> events // Default to no sorting if unexpected
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                }
            })
        }
    ) { innerPadding ->
        // --- Integration of new data logic within the existing layout ---
        Box( // Use a Box to center content based on state
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center // Center content within the Box
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator() // Show loading indicator
                }
                error != null -> {
                    // Show error message and a retry button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error loading events: $error", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchEvents() }) { // Call ViewModel's fetch method
                            Text("Retry")
                        }
                    }
                }
                sortedEvents.isNotEmpty() -> {
                    // Display the sorted list of events using LazyColumn
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        // If your Event class had an ID, you would use key = { it.id }
                        items(sortedEvents) { event ->
                            EventCard(event = event) // Use your existing EventCard composable
                        }
                    }
                }
                else -> {
                    // Show message when there are no events
                    Text("No events found.")
                }
            }
        }
        // --- End of integration ---
    }
}


// Your existing EventCard composable (included here for completeness)
@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Problem point: mainImageUrl is used with painterResource, assuming it's a drawable ID
            // If your backend returns a URL, you need to use AsyncImage here.

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = event.date,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Ensure you have R.string.participants defined in your strings.xml
                    Text(
                        text = "${event.going} ${stringResource(R.string.participants)}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f) // This weight might cause layout issues, consider if it's needed
                    )
                }
            }
        }
    }
}

fun sortEvents(
    eventList: List<Event>,
    option: SortOption
): List<Event> { // Sortiere die Liste basierend auf der ausgewählten Option
    return when (option) {
        SortOption.NONE -> eventList
        SortOption.DATE_ASC -> eventList.sortedBy { it.date }
        SortOption.DATE_DESC -> eventList.sortedByDescending { it.date }
        SortOption.TITLE -> eventList.sortedBy { it.title.lowercase() }
        SortOption.LOCATION -> eventList.sortedBy { it.location.lowercase() }
        SortOption.GOING -> eventList.sortedBy { it.going }
    }
}
