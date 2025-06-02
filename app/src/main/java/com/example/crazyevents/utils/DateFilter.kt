package com.example.crazyevents.utils

import android.app.DatePickerDialog
import android.os.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

@Composable
fun DateFilter(selected: LocalDate?, onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    val today = LocalDate.now()

    Button(onClick = {
        val picker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, dayOfMonth, month))
            },
            today.year, today.monthValue - 1, today.dayOfMonth
        )
        picker.show()
    }) {
        Text(text = selected?.toString() ?: "Datum wählen")
    }
}
