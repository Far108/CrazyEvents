package com.example.crazyevents.presentation


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crazyevents.data.Poster
import com.example.crazyevents.model.PosterViewModel


@Composable
fun ExploreScreen(viewModel: PosterViewModel = viewModel()) {
    val posters by viewModel.posters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPosters() // Trigger only once when Composable is first shown
    }

    when {
        isLoading -> CircularProgressIndicator()
        error != null -> Text("Error: $error")
        posters.isEmpty() -> Text("No posters found.")
        else -> {
            Text(text = "SearchPage")
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posters) { poster ->
                    PosterCard(viewModel, poster)
                }
            }
        }
    }
}

@Composable
fun PosterCard(viewModel: PosterViewModel, poster: Poster) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(text = "Bild")
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(poster.name, style = MaterialTheme.typography.titleMedium)
                Text(poster.description, style = MaterialTheme.typography.bodySmall)
                Text("Category: ${poster.category}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                var checked by remember { mutableStateOf(poster.isFollowed) }

                Text(text = if (checked) "Follows" else "Unfollows", style = MaterialTheme.typography.bodySmall)
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        viewModel.updateFollowStatus(poster.id)
                        /*
                        Toast.makeText(
                            context,
                            if (it) "Following ${poster.name}" else "Unfollowed ${poster.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                         */
                  },
                )
            }
        }
    }
}
