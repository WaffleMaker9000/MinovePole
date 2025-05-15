package com.example.minovepole

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHost
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
                }
            )
        }

        composable("difficulty") {
            DifficultyScreen(
                mineDifficultySelected = mineDifficultySelected,
                onMineDifficultySelected = { mineDifficultySelected = it },
                sizeDifficultySelected = sizeDifficultySelected,
                onSizeDifficultySelected = { sizeDifficultySelected = it},
                onConfirm = {}
            )
        }
    }
}