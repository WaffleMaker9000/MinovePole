package com.example.minovepole

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight

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
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = stringResource(R.string.select_diff),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
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
            Spacer(modifier = Modifier.weight(0.5f))
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
            Spacer(modifier = Modifier.weight(0.5f))
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = stringResource(R.string.confirm), color = Color.White)
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}