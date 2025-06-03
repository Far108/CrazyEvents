package com.example.crazyevents.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.crazyevents.data.Poster
import com.example.crazyevents.model.PosterViewModel

@Composable
fun PosterCard(viewModel: PosterViewModel, poster: Poster, onClick: () -> Unit) {
    val context = LocalContext.current
    val checked = poster.follow

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp), // Weniger Höhe
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = poster.name,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (checked) "Follow" else "Not Follow",
                    style = MaterialTheme.typography.labelSmall
                )
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        viewModel.updateFollowStatus(poster.id, context)
                        viewModel.fetchPosters(context)
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
