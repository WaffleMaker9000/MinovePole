package com.minovepole.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.minovepole.logic.GameViewModel
import com.minovepole.data.DifficultyOption
import com.minovepole.logic.DifficultySelectViewModel
import com.minovepole.ui.DifficultyScreen
import com.minovepole.ui.Game
import com.minovepole.ui.LeaderBoard
import com.minovepole.ui.MainMenu
import kotlinx.coroutines.flow.collect

/**
 * Defines the navigation graph for the application
 * using the Navigation component of Jetpack Compose.
 *
 * @param navController The NavHostController that manages navigation between composable screens.
 */
@Composable
fun AppNavGraph(navController: NavHostController) {
    // Shared ViewModel for difficulty selection screen
    val difficultySelectViewModel: DifficultySelectViewModel = viewModel()
    val mineDifficultySelected by difficultySelectViewModel.mineDifficulty.collectAsState()
    val sizeDifficultySelected by difficultySelectViewModel.sizeDifficulty.collectAsState()

    NavHost(navController = navController, startDestination = "main_menu") {
        // Main menu screen with options to navigate to the game or leaderboard
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

        // Screen for selecting mine and size difficulties before launching the game
        composable("difficulty") {
            DifficultyScreen(
                mineDifficultySelected = mineDifficultySelected,
                onMineDifficultySelected = difficultySelectViewModel::onMineDifficultyChange,
                sizeDifficultySelected = sizeDifficultySelected,
                onSizeDifficultySelected = difficultySelectViewModel::onSizeDifficultyChange,
                onConfirm = {
                    navController.navigate("game")
                }
            )
        }

        // Leaderboard screen
        composable("leaderboard") {
            LeaderBoard(context = LocalContext.current)
        }

        // The game screen, uses difficulty settings selected in the difficulty screen
        composable("game") {
            val viewModel: GameViewModel = viewModel()

            Game(
                mineDiff = mineDifficultySelected.ordinal,
                sizeDiff = sizeDifficultySelected.ordinal,
                viewModel = viewModel,
                onMenuClick = {
                    // Pop back to menu
                    navController.popBackStack()
                    navController.popBackStack()
                },
                onLeaderBoardClick = {
                    // Pop back to menu, then go to leaderboard
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("leaderboard")
                }
            )
        }
    }
}