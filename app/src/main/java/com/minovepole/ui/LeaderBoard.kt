package com.minovepole.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minovepole.R
import com.minovepole.data.DifficultyOption
import com.minovepole.data.clearScores
import com.minovepole.data.readScores
import com.minovepole.logic.LeaderBoardViewModel

/**
 * A composable that displays the leaderboard screen, with selectors to filter by difficulties
 * and a lazy column with a header to display scores
 *
 * @param context Application context, used to load saved scores
 */
@Composable
fun LeaderBoard (
    context: Context
) {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        // The viewmodel is created before it has any scores loaded
        val viewModel: LeaderBoardViewModel = viewModel()
        val mineDifficultySelected by viewModel.mineDifficulty.collectAsState()
        val sizeDifficultySelected by viewModel.sizeDifficulty.collectAsState()

        // On launch, load scores and supply them to the viewModel
        LaunchedEffect(Unit) {
            val scores = readScores(context)
            viewModel.setScores(scores)
        }

        // View model is then observed
        val allScores by viewModel.allScores.collectAsState()

        // Scores are filtered by difficulty selections
        val selectedScores = allScores.filter {
            it.mineDifficulty == mineDifficultySelected && it.sizeDifficulty == sizeDifficultySelected
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            // Title
            Text(
                text = stringResource(R.string.leaderboard_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))

            // Mine difficulty selector
            Text(
                text = stringResource(R.string.mine_diff),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            DifficultyButtons(
                options = DifficultyOption.entries,
                selectedOption = mineDifficultySelected,
                onOptionSelected = viewModel::onMineDifficultyChanged
            )
            Spacer(modifier = Modifier.weight(0.5f))

            // Size difficulty selector
            Text(
                text = stringResource(R.string.size_diff),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            DifficultyButtons(
                options = DifficultyOption.entries,
                selectedOption = sizeDifficultySelected,
                onOptionSelected = viewModel::onSizeDifficultyChanged
            )
            Spacer(modifier = Modifier.weight(1f))

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.placed),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = stringResource(R.string.player),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.time),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }

            // Lazy column that displays scores, and assigns them an order
            LazyColumn (
                modifier = Modifier.fillMaxWidth().weight(20f)
            ) {
                itemsIndexed(selectedScores) { index, score ->
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

            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                // Clear scores button
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    onClick = {
                        clearScores(context)
                        viewModel.clearScores()
                    }
                ) {
                    Text(text = stringResource(R.string.clear))
                }
            }
        }
    }
}