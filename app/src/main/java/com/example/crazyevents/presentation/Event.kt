package com.example.crazyevents.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.crazyevents.data.Event


@Composable
fun Event(
    event: Event
) {

    // Fullscreen layout with content alignment
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = event.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Placeholder image banner
        Image(
            painter = painterResource(id = com.example.crazyevents.R.drawable.logo),
            contentDescription = "Event Banner Placeholder",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Location and Date
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Location: ${event.location}")
            Text(text = "Datum: ${event.date}")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Scrollable description box
        Text(text = "Beschreibung:", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(6.dp))
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            Text(text = event.description)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visitors + Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Besucher: ${event.going ?: 0}")
            Button(onClick = { /* anzeigen */ }) {                //TODO: Add User to Event Visitor List
                Text("Besuchen!")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Placeholder photo row
        Text("Fotos", style = MaterialTheme.typography.titleMedium)
        LazyRow {
            items(5) { index ->
                Image(
                    painter = painterResource(id = com.example.crazyevents.R.drawable.logo),
                    contentDescription = "Foto $index",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
