package com.minovepole.ui

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
import com.minovepole.data.DifficultyOption

/**
 * Composable that displays "radio buttons" or "exclusive buttons", one for each difficulty option
 *
 * Is used for both the difficulty select screen, and the leaderboards screen, so it has its own
 * file
 *
 * @param options A list of the options, will display a button for each
 * @param selectedOption The default option to be selected on composition, also tracks state
 * @param onOptionSelected Callback that is called when an option is selected
 */
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