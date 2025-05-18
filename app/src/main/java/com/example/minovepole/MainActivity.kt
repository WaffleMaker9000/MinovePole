package com.example.minovepole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.minovepole.ui.theme.MinovePoleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //generateTestScores(this)
        enableEdgeToEdge()
        setContent {
            MinovePoleTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}