package com.example.crazyevents.presentation

import android.app.DatePickerDialog
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable     //ChatGPT
fun FilterSection(
    isVisible: Boolean,
    selectedDate: LocalDate?,
    onDateChange: (LocalDate) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    selectedCategory: CategoriesEnum,
    onCategoryChange: (CategoriesEnum) -> Unit
) {

    if (!isVisible) return          //TODO anders lösen

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val context = LocalContext.current

    val today = remember { LocalDate.now() }

    val datePicker = remember {
        DatePickerDialog(context).apply {
            setOnDateSetListener { _, year, month, day ->
                onDateChange(LocalDate.of(year, month + 1, day))
            }
        }
    }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { datePicker.show() }) {
                    Text("Datum wählen")
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = onLocationChange,
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CategoryDropdown(
                selected = selectedCategory,
                onCategorySelected = onCategoryChange
            )
        }
    }
