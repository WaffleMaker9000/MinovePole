package com.example.minovepole

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun DifficultyButtons(
    options: List<DifficultyOption>,
    selectedOption: DifficultyOption,
    onOptionSelected: (DifficultyOption) -> Unit
) {
    Row (
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            Button(
                onClick = {onOptionSelected(option)},
                colors = if (option == selectedOption) {
                    ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                } else {
                    ButtonDefaults.buttonColors(containerColor = Color.Gray)
                }
            ) {
                Text(text = stringResource(id = option.labelResId), color = Color.White)
            }
        }
    }
}