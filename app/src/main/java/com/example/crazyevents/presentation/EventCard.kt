package com.example.crazyevents.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.crazyevents.R
import com.example.crazyevents.data.Event

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Problem point: mainImageUrl is used with painterResource, assuming it's a drawable ID
            // If your backend returns a URL, you need to use AsyncImage here.

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = event.date,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = event.location,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Ensure you have R.string.participants defined in your strings.xml
                    Text(
                        text = "${event.going} ${stringResource(R.string.participants)}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f) // This weight might cause layout issues, consider if it's needed
                    )
                }
            }
        }
    }
}


@Preview(name = "EventCard")
@Composable
private fun PreviewEventCard() {
    EventCard(
        Event(
            title       = "SAMPLE Festival",
            description = "Ein buntes Open-Air-Konzert im Stadtpark",
            location    = "Stadtpark",
            address     = "iwo",
            creator     = "John Doe",
            date        = "2025-06-21 18:00",
            category    = "Music",
            going = 1
        ),
    )
}