package com.example.minovepole

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController) {
    var mineDifficultySelected by remember { mutableStateOf(DifficultyOption.MEDIUM) }
    var sizeDifficultySelected by remember { mutableStateOf(DifficultyOption.MEDIUM) }

    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenu(
                onPlayClick = {
                    navController.navigate("difficulty")
                },
                onLeaderBoardClick = {
                    navController.navigate("leaderboard")
                }
            )
        }

        composable("difficulty") {
            DifficultyScreen(
                mineDifficultySelected = mineDifficultySelected,
                onMineDifficultySelected = { mineDifficultySelected = it },
                sizeDifficultySelected = sizeDifficultySelected,
                onSizeDifficultySelected = { sizeDifficultySelected = it},
                onConfirm = {
                    navController.navigate("game")
                }
            )
        }

        composable("leaderboard") {
            LeaderBoard(context = LocalContext.current)
        }

        composable("game") {
            val x = 4 + (sizeDifficultySelected.ordinal * 2)
            val mineCount = 6 + ((mineDifficultySelected.ordinal + sizeDifficultySelected.ordinal) * 2)

            val viewModel: GameViewModel = viewModel()

            Game(x = x, y = x * 2, mineCount = mineCount, viewModel = viewModel)
        }
    }
}