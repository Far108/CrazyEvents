package com.example.crazyevents.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.crazyevents.data.Event
import com.example.crazyevents.model.MainScreenViewModel
import com.example.crazyevents.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel(), // Use the MainViewModel
    navHostController: NavHostController
    // Assuming filterButton and SortMenu are defined elsewhere
) {
    // Collect the state flows from the ViewModel
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val reload by viewModel.reloadEventsTrigger.collectAsState()
    var currentSort by remember { mutableStateOf(SortOption.NONE) } // Default sort


    LaunchedEffect(reload) {
        if (reload) {
            viewModel.fetchEvents()
            viewModel.resetReloadTrigger()
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
                    SortMenu(selected = currentSort) { newSort ->
                        currentSort = newSort
                        viewModel.insertEvents(sortEvents(events, currentSort))
                    }

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

                events.isNotEmpty() -> {
                    // Display the sorted list of events using LazyColumn
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        // If your Event class had an ID, you would use key = { it.id }
                        items(events) { event ->
                            EventCard(event = event) {navHostController.navigate(Screen.EventView.createRoute(event.id))}
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


fun sortEvents(
    eventList: List<Event>,
    option: SortOption
): List<Event> {
    return when (option) {
        SortOption.NONE -> eventList
        SortOption.DATE_ASC -> eventList.sortedBy { it.date }
        SortOption.DATE_DESC -> eventList.sortedByDescending { it.date }
        SortOption.TITLE -> eventList.sortedBy { it.title.lowercase() }
        SortOption.LOCATION -> eventList.sortedBy { it.location.lowercase() }
        SortOption.GOING -> eventList.sortedBy { it.going }
    }
}