package com.example.minovepole

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Preview (showBackground = true)
@Composable
fun DifficultyScreenPreview(){
    var mineDifficultySelected by remember { mutableStateOf(DifficultyOption.MEDIUM) }
    var sizeDifficultySelected by remember { mutableStateOf(DifficultyOption.MEDIUM) }

    DifficultyScreen(
        mineDifficultySelected = mineDifficultySelected,
        onMineDifficultySelected = { mineDifficultySelected = it},
        sizeDifficultySelected = sizeDifficultySelected,
        onSizeDifficultySelected = {sizeDifficultySelected = it},
        onConfirm = {}
    )
}

@Composable
fun DifficultyScreen(
    mineDifficultySelected: DifficultyOption,
    onMineDifficultySelected: (DifficultyOption) -> Unit,
    sizeDifficultySelected: DifficultyOption,
    onSizeDifficultySelected: (DifficultyOption) -> Unit,
    onConfirm: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.select_diff),
                style = MaterialTheme.typography.headlineLarge,

            )
            Text(
                text = stringResource(R.string.mine_diff),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            DifficultyButtons(
                options = DifficultyOption.entries,
                selectedOption = mineDifficultySelected,
                onOptionSelected = onMineDifficultySelected
            )
            Text(
                text = stringResource(R.string.size_diff),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            DifficultyButtons(
                options = DifficultyOption.entries,
                selectedOption = sizeDifficultySelected,
                onOptionSelected = onSizeDifficultySelected
            )
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    }
}

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
                Text(text = stringResource(id = option.labelResId))
            }
        }
    }
}

enum class DifficultyOption(@StringRes val labelResId: Int) {
    EASY(R.string.easy_option),
    MEDIUM(R.string.medium_option),
    HARD(R.string.hard_option)
}

