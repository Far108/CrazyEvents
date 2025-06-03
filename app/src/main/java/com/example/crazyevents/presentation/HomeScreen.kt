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
import com.example.crazyevents.model.ExploreViewModel
import com.example.crazyevents.model.HomeScreenViewModel
import com.example.crazyevents.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    filterViewModel: ExploreViewModel = viewModel(),
    navHostController: NavHostController
) {

    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val reload by viewModel.reloadEventsTrigger.collectAsState()
    var currentSort by remember { mutableStateOf(SortOption.DATE_DESC) } // Default sort
    var context = LocalContext.current
    var filterVisible by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
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
                Column(modifier = Modifier.fillMaxWidth()) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { filterVisible = !filterVisible }) {
                            Text("Filter")
                        }

                        SortMenu(selected = currentSort) { currentSort = it }
                    }

                    Spacer(modifier = Modifier.height(4.dp))


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
                        selectedDate = selectedDate,
                        onDateChange = { selectedDate = it },
                        location = location,
                        onLocationChange = { location = it },
                        selectedCategory = category,
                        onCategoryChange = { category = it }
                    )
                }
            }
            item() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        viewModel.applyFiltersAndSort(
                            category = if (category == CategoriesEnum.ALL) null else category.label,
                            location = location,
                            date = selectedDate,
                            sort = currentSort
                        )
                    }) {
                        Text("Suchen")
                    }

                    Button(onClick = {
                        category = CategoriesEnum.ALL
                        location = ""
                        selectedDate = null
                        viewModel.fetchMyEvents(context)
                    }) {
                        Text("Zurücksetzen")
                    }
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
                            Button(onClick = { viewModel.fetchMyEvents(context) }) {
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