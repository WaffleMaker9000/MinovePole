package com.minovepole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.minovepole.navigation.AppNavGraph
import com.minovepole.sound.SoundManager
import com.minovepole.ui.theme.MinovePoleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //generateTestScores(applicationContext)
        SoundManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MinovePoleTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}