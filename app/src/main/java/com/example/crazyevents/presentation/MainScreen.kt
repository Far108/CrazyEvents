package com.example.crazyevents.presentation

import android.R.attr.maxLines
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.crazyevents.R
import com.example.crazyevents.dataLayer.Event
import com.example.crazyevents.dataLayer.getDummyEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(eventList: List<Event> = getDummyEvents()) {

    var currentSort by remember { mutableStateOf(SortOption.NONE) }

    val sortedEvents = remember(currentSort) {
        sortEvents(eventList, currentSort)
    }


    @Composable
    fun filterButton() {
        Text("Filtering here")
    }


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
                Image(
                    painter = painterResource(id = event.mainImageUrl),
                    contentDescription = "Event Logo",
                    modifier = Modifier.size(64.dp)
                )

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

                        Text(
                            text = "${event.going} ${stringResource(R.string.participants)}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }



    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    filterButton()
                    SortMenu(selected = currentSort) { newSort ->
                        currentSort = newSort
                    }
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(sortedEvents) { event ->
                EventCard(event = event)
            }
        }
    }
}


fun sortEvents(
    eventList: List<Event>,
    option: SortOption
): List<Event> { // Sortiere die Liste basierend auf der ausgewählten Option
    return when (option) {
        SortOption.NONE -> eventList
        SortOption.DATE_ASC -> eventList.sortedBy { it.date }
        SortOption.DATE_DESC -> eventList.sortedByDescending { it.date }
        SortOption.TITLE -> eventList.sortedBy { it.title.lowercase() }
        SortOption.LOCATION -> eventList.sortedBy { it.location.lowercase() }
        SortOption.GOING -> eventList.sortedBy { it.going }
    }
}
