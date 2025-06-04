package com.example.crazyevents.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.crazyevents.data.Event
import com.example.crazyevents.login.UserSession
import com.example.crazyevents.model.EventViewModel
import com.example.crazyevents.model.ExploreViewModel
import com.example.crazyevents.model.PosterViewModel


@Composable
fun Event(
    event: Event
) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val eventViewModel: ExploreViewModel = viewModel(activity)
    val viewModel: EventViewModel = viewModel()
    val followModel: PosterViewModel = viewModel(activity)
    val interestedEvents by eventViewModel.interestedEvents.collectAsState()
    val isInterested = interestedEvents.contains(event.id)
    var isFollowButtonVisible by remember { mutableStateOf(true) }

    // Picture Upload
    // Picture Upload
    val currentUserId = UserSession.currentUser?.id
    val isCreator = currentUserId == event.creator?.id

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                viewModel.uploadEventImages(eventId = event.id, uris = uris, context = context)
                viewModel.uploadEventImages(event.id, uris, context)
                viewModel.getEventById(event.id)

            }
        }
    )


    val numberOfVisitors by eventViewModel.numberOfInterests.collectAsState()

    LaunchedEffect(Unit) {
        eventViewModel.updatedNumberOfVisitors(event, context)
        eventViewModel.loadInterestedEvents(context)
        event.creator?.id?.let { isFollowButtonVisible=followModel.isFollowing(event.creator.id, context) } ?: true
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
            AsyncImage(
                model = event.mainImageUrl,
                contentDescription = "Event Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
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


            event.websiteUrl?.let { url ->
                Text(
                    text = url,
                    style = TextStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }


            //Poster of this Event

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {
                        shareText(
                            context,
                            "Hi, check das aus: ${event.title} bei ${event.location} am ${event.date}. Mehr infos: ${event.description}."
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Event teilen")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Event teilen")
                }

                Button(
                    enabled = !isFollowButtonVisible,
                    onClick = {
                        followModel.updateFollowStatus(event.creator?.id.toString(), context = context)
                        isFollowButtonVisible = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Poster folgen")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Folgen")
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
                Button(onClick = { eventViewModel.updateEventInterest(event.id, context = context) }) {
                    Text(if (!isInterested) "Besuchen!" else "Nicht interessiert")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nur Creator sieht diesen Button
            if (isCreator) {
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Bilder hinzufügen")
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Placeholder photo row
            Text("Fotos", style = MaterialTheme.typography.titleMedium)
            LazyRow {
                items(event.gallery.size) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Event Foto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}

fun shareText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Teilen über")
    context.startActivity(shareIntent)
}