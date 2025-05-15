package com.example.minovepole

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main_menu") {
        composable("main_menu") {
            MainMenu(
                onPlayClick = {
                    navController.navigate("difficulty")
                }
            )
        }

        composable("difficulty") {
            DifficultyScreen(
                mineDifficultySelected = DifficultyOption.MEDIUM,
                onMineDifficultySelected = {},
                sizeDifficultySelected = DifficultyOption.MEDIUM,
                onSizeDifficultySelected = {},
                onConfirm = {}
            )
        }
    }
}