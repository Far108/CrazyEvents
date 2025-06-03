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
import com.example.crazyevents.model.PosterViewModel
import com.example.crazyevents.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowScreen(
    viewModel: PosterViewModel = viewModel(), // Use the PosterViewModel
    navHostController: NavHostController
) {
    // Collect the state flows from the ViewModel
    val posters by viewModel.posters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val reload by viewModel.reloadEventsTrigger.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(reload) {
        if (reload) {
            viewModel.fetchPosters(context)
            viewModel.resetReloadTrigger()
        }
    }
    Text("Abos")
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
                        Button(onClick = { viewModel.fetchPosters(context) }) { // Call ViewModel's fetch method
                            Text("Retry")
                        }
                    }
                }

                posters.isNotEmpty() -> {
                    // Display the sorted list of events using LazyColumn
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        // If your Event class had an ID, you would use key = { it.id }
                        items(posters) { poster ->
                            PosterCard(viewModel, poster = poster) { navHostController.navigate(Screen.EventView.createRoute(poster.id)) }
                        }
                    }
                }

                else -> {
                    // Show message when there are no events
                    Text("No posters found.")
                }
            }
        }
        // --- End of integration ---
    }
}