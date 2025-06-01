package com.example.crazyevents.presentation


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


@Composable
fun ExploreScreen(viewModel: ExploreViewModel = viewModel(),
                  navHostController: NavHostController) {
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
                    ExploreCard(viewModel, event) {
                        navHostController.navigate(
                            Screen.EventView.createRoute(
                                event.id
                            )
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ExploreCard(viewModel: ExploreViewModel, event: Event, onClick: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (viewModel.interestedEvents.value.isEmpty()) {
            viewModel.loadInterestedEvents(context)
        }
    }

    // ViewModel-Zustand abonnieren
    val interestedEvents by viewModel.interestedEvents.collectAsState()

    // Der Zustand kommt jetzt aus dem ViewModel
    val checked = interestedEvents.contains(event.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, style = MaterialTheme.typography.titleMedium)
                Text(event.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Text("Category: ${event.category}", style = MaterialTheme.typography.bodySmall)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (checked) "Going" else "Not Going",
                    style = MaterialTheme.typography.bodySmall
                )
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



