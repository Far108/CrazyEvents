package com.example.crazyevents.presentation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.crazyevents.navigation.Screen
import com.example.crazyevents.utils.CategoryFilter
import com.example.crazyevents.utils.DateFilter
import java.time.LocalDate


@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = viewModel(),
    navHostController: NavHostController
) {
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Filter- und Sortier-States
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedSort by remember { mutableStateOf(SortOption.NONE) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchExploreEvents()
        viewModel.loadInterestedEvents(context)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        // Filter UI
        CategoryFilter(selectedCategory) { selectedCategory = it }
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = selectedLocation,
            onValueChange = { selectedLocation = it },
            label = { Text("Ort") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))

        // DateFilter(selectedDate) { selectedDate = it }
        Spacer(modifier = Modifier.height(4.dp))

        SortMenu(selected = selectedSort) { selectedSort = it }
        Spacer(modifier = Modifier.height(4.dp))

        Button(onClick = {
            viewModel.applyFiltersAndSort(
                category = selectedCategory,
                location = selectedLocation,
                date = selectedDate,
                sort = selectedSort
            )
        }) {
            Text("Suchen")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Ergebnisanzeige
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text("Fehler: $error")
            events.isEmpty() -> Text("Keine passenden Events gefunden.")
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(events) { event ->
                    ExploreCard(viewModel, event) {
                        navHostController.navigate(Screen.EventView.createRoute(event.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ExploreCard(viewModel: ExploreViewModel, event: Event, onClick: () -> Unit) {
    val context = LocalContext.current
    val interestedEvents by viewModel.interestedEvents.collectAsState()
    val checked = interestedEvents.contains(event.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, style = MaterialTheme.typography.titleMedium)
                Text(event.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Text("Typ: ${event.category}", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (checked) "Going" else "Not Going")
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        viewModel.updateEventInterest(event.id, context)
                    }
                )
            }
        }
    }
}




