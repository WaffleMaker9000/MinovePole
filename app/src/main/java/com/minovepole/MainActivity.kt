package com.minovepole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.minovepole.navigation.AppNavGraph
import com.minovepole.sound.SoundManager
import com.minovepole.ui.theme.MinovePoleTheme

/**
 * Class that runs the game, initialises the SoundManager, and runs the navController
 *
 * On app close, it also releases the SoundManager
 */
class MainActivity : ComponentActivity() {
    /**
     * Function that is run when the app starts
     *
     * @param savedInstanceState Default, but im pretty sure its the state of the suspended app
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mock leaderboard data creation
        // generateTestScores(applicationContext)

        // Initialise SoundManager
        SoundManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MinovePoleTheme {
                // Run the navController
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }

    /**
     * Function that is called when the app closes, releases the SoundManager
     */
    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
    }
}