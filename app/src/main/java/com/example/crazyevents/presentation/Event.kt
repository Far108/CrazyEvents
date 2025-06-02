package com.example.crazyevents.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crazyevents.data.Event
import com.example.crazyevents.model.ExploreViewModel


@Composable
fun Event(
    event: Event
) {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: ExploreViewModel = viewModel(activity)
    val interestedEvents by viewModel.interestedEvents.collectAsState()
    val isInterested = interestedEvents.contains(event.id)

    val numberOfVisitors by viewModel.numberOfInterests.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updatedNumberOfVisitors(event, context)
        viewModel.loadInterestedEvents(context)
    }


    // Fullscreen layout with content alignment
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

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


            //Poster of this Event

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = com.example.crazyevents.R.drawable.logo),
                        contentDescription = "Profile Picture Poster",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = event.creator?.name ?: "Unknown")
                }
                Button(onClick = { /* folgen */ }) {            //TODO: Follow logik hier
                    Text("Folgen!")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            // Visitors + Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Besucher: $numberOfVisitors")
                Button(onClick = { viewModel.updateEventInterest(event.id, context = context) }) {
                    Text(if (!isInterested) "Besuchen!" else "Nicht interessiert")
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


        }
    }
}
