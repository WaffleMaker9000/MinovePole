package com.example.minovepole

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview (showBackground = true)
@Composable
fun GamePreview () {
    Game()
}

@Composable
fun Game (
    viewModel: GameViewModel = viewModel()
) {
    val time by viewModel.time
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.timer)
            )
            Text(
                text = time.toString()
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }
}