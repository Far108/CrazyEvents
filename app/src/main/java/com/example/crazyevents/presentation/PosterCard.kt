package com.example.crazyevents.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
    val checked = poster.follow;
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
                Text(poster.name, style = MaterialTheme.typography.titleMedium)
//                Text(poster.id, style = MaterialTheme.typography.titleMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (checked) "Follow" else "Not Follow")
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        viewModel.updateFollowStatus(poster.id, context)
                        Thread.sleep(300)
                        viewModel.fetchPosters(context)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
    }
}