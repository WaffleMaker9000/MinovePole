package com.example.minovepole

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview (showBackground = true)
@Composable
fun LeaderBoardPreview (

) {
    LeaderBoard()
}

@Composable
fun LeaderBoard (

) {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        var mineDifficultySelected by remember { mutableStateOf(DifficultyOption.MEDIUM) }
        var sizeDifficultySelected by remember { mutableStateOf(DifficultyOption.MEDIUM) }

        val scores = remember {
            listOf(
                Score("Waffl", 42),
                Score("Tom", 55),
                Score("Yusuf", 60),
                Score("Ghazghkhull", 75),
                Score("Balal", 80),
            ).sortedBy { it.time }
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.leaderboard_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
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
                onOptionSelected = {mineDifficultySelected = it }
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
                onOptionSelected = {sizeDifficultySelected = it }
            )
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Placed",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Player",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Time",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }

            LazyColumn (
                modifier = Modifier.fillMaxWidth().weight(20f)
            ) {
                itemsIndexed(scores) { index, score ->
                    Card (
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Gray)
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${index + 1}",
                                textAlign = TextAlign.Start,
                                color = Color.White,
                            )
                            Text(
                                text = score.name,
                                textAlign = TextAlign.Center,
                                color = Color.White,
                            )
                            Text(
                                text = score.time.toString(),
                                textAlign = TextAlign.End,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}