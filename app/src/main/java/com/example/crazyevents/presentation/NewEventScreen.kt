package com.example.crazyevents.presentation


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crazyevents.data.Creator
import com.example.crazyevents.data.Event
import com.example.crazyevents.login.UserSession
import com.example.crazyevents.model.MainScreenViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    viewModel: MainScreenViewModel = viewModel(), // Use the MainViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<CategoriesEnum?>(null) }


    var showError by remember { mutableStateOf(false) }
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var showDialExample by remember { mutableStateOf(false) }

    val creator: Creator? = UserSession.currentUser?.let {
        Creator(
            id = UserSession.currentUser!!.id,
            name = UserSession.currentUser!!.name
        )
    }

    val submitMessage by viewModel.submitMessage.collectAsState()

    val datePattern = Regex("""\d{4}-\d{2}-\d{2} \d{2}:\d{2}""")
    val isDateValid = datePattern.matches(date)

    val allFieldsValid = title.isNotBlank() &&
            description.isNotBlank() &&
            location.isNotBlank() &&
            address.isNotBlank() &&
            selectedCategory != null &&
            isDateValid

    if(allFieldsValid) showError = false

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "New event", style = MaterialTheme.typography.bodyMedium)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            isError = showError && title.isBlank(),
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            isError = showError && description.isBlank(),
            singleLine = false,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            )
        )

        /*
                DialUseStateExample(
                    onDismiss = {
                        showDialExample = false
                    },
                    onConfirm = {
                            time ->
                        selectedTime = time
                        date = formatter.format(
                            Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, time.hour)
                                set(Calendar.MINUTE, time.minute)
                            }.time
                        )
                        showDialExample = false
                    },
                )
        */
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            isError = showError && location.isBlank(),
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            isError = showError && address.isBlank(),
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (yyyy-MM-dd HH:mm)") },
            isError = showError && !isDateValid,
            singleLine = true,
            supportingText = {
                if (showError && !isDateValid) {
                    Text("Use format: yyyy-MM-dd HH:mm", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        CategoryDropdown(
            selected = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            modifier = Modifier.fillMaxWidth()
        )


        val submitMessage by viewModel.submitMessage.collectAsState()

        Button(
            onClick = {
                showError = true
                if (allFieldsValid) {
                    viewModel.addEvent(
                        Event(
                            title = title,
                            description = description,
                            location = location,
                            address = address,
                            date = date,
                            goingUserIds = emptyList(),
                            category = selectedCategory?.name ?: "",
                            id = "",
                            creator = creator,
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

        // Fehler bei Validierung (z. B. leeres Feld)
        if (showError) {
            Text(
                text = "Bitte fülle alle Pflichtfelder aus.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Erfolgs- oder Fehlermeldung vom ViewModel
        submitMessage?.let { message ->
            Text(
                text = message,
                color = if (message.startsWith("✅")) Color.Green else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

