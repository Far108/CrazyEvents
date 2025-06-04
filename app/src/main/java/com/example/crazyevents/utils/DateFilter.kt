package com.example.crazyevents.utils

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DateFilter(
    selected: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val today = LocalDate.now()

    OutlinedButton(
        onClick = {
            val picker = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    try {
                        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        onDateSelected(selectedDate)
                    } catch (e: Exception) {
                        Log.e("DateFilter", "Ungültiges Datum: ${e.message}")
                    }
                },
                today.year,
                today.monthValue - 1,
                today.dayOfMonth
            )
            picker.show()
        },
        shape = RoundedCornerShape(12.dp), // leicht runder
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = selected?.toString() ?: "Datum wählen",
            style = MaterialTheme.typography.bodySmall
        )
    }
}


