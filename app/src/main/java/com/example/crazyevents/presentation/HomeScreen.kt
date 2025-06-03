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
import androidx.navigation.NavHostController
import com.example.crazyevents.data.Event
import com.example.crazyevents.model.HomeScreenViewModel
import com.example.crazyevents.navigation.Screen
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(), // Use the MainViewModel
    navHostController: NavHostController
    // Assuming filterButton and SortMenu are defined elsewhere
) {
    // Collect the state flows from the ViewModel
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val reload by viewModel.reloadEventsTrigger.collectAsState()
    var currentSort by remember { mutableStateOf(SortOption.DATE_DESC) } // Default sort
    var context = LocalContext.current
    var filterVisible by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var location by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(CategoriesEnum.ALL) }


    LaunchedEffect(reload) {
        if (reload) {
            viewModel.fetchMyEvents(context)
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

                    Button(onClick = { filterVisible = !filterVisible }) {
                        Text("Filter")
                    }

                    SortMenu(selected = currentSort) { newSort ->
                        currentSort = newSort
                        viewModel.insertEvents(sortEvents(events, currentSort))
                    }

                }
            })
        }
    ) { innerPadding ->
        // --- Integration of new data logic within the existing layout ---
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (filterVisible) {
                item {
                    FilterSection(
                        isVisible = filterVisible,
                        startDate = startDate,
                        endDate = endDate,
                        onStartDateChange = { startDate = it },
                        onEndDateChange = { endDate = it },
                        location = location,
                        onLocationChange = { location = it },
                        selectedCategory = category,
                        onCategoryChange = { category = it }
                    )
                }
            }


            when {
                isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                error != null -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Error loading events: $error",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.fetchEvents() }) {
                                Text("Retry")
                            }
                        }
                    }
                }

                events.isNotEmpty() -> {
                    items(events) { event ->
                        EventCard(event = event) {
                            navHostController.navigate(
                                Screen.EventView.createRoute(event.id)
                            )
                        }
                    }
                }

                else -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No events found.")
                        }
                    }
                }
            }
        }
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