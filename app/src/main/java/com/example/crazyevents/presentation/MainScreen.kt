package com.example.crazyevents.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crazyevents.Event

@Composable
fun MainScreen() {






    val hardcodedEvents = listOf(
        Event(
            id = 1,
            name = "Tech Conference 2025",
            date = "2025-05-07",
            location = "Marx-Halle, 1030 Wien"
        ),
        Event(id = 2, name = "Sommerfest", date = "2025-08-15", location = "Stadtpark, 1020 Wien"),
        Event(
            id = 3,
            name = "Sommernachtskonzert der Wiener Philharmoniker",
            date = "2025-06-13",
            location = "Schloss Schoenbrunn, 1130 Wien"
        )
    )


    @Composable
    fun EventItem(event: Event) {
        Card(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Name: ${event.name}")
                Text(text = "Date: ${event.date}")
                Text(text = "Location: ${event.location}")
            }
        }
    }

    @Composable
    fun HardcodedEventsScreen() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(hardcodedEvents) { event ->
                    EventItem(event = event)
                }
            }
        }


    }

    HardcodedEventsScreen()
}
