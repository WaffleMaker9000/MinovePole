package com.minovepole.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.minovepole.R
import com.minovepole.ui.theme.MinovePoleTheme

/**
 * Composable that displays the main menu screen, with buttons linking to difficulty selection
 * and the leaderboard
 *
 * @param modifier Im not completely sure why i passed this here,
 * im never passing it a modifier anyway, but im scared to break it, so ill keep it here
 * @param onPlayClick Callback that is called when the play button is clicked
 * @param onLeaderBoardClick Callback that is called when the leaderboard button is clicked
 */
@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit = {},
    onLeaderBoardClick: () -> Unit = {}
) {
    Surface (
        modifier = modifier
            .fillMaxSize(),
        color = Color.LightGray
    ) {
        // Used to get access to the screen width for button scaling
        BoxWithConstraints (
            modifier = Modifier.fillMaxSize()
        ) {
            val buttonSize = maxWidth * 0.35f

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                // Title
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(0.5f))

                // Image
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(180.dp)
                )
                Spacer(modifier = modifier.weight(1f))

                // Button row
                Row (
                    horizontalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    // Start button image
                    Image(
                        painter = painterResource(R.drawable.play),
                        contentDescription = "Start Button",
                        modifier = Modifier
                            .size(buttonSize)
                            .clickable { onPlayClick() }
                    )
                    // Leaderboard button image
                    Image(
                        painter = painterResource(R.drawable.leaderboard),
                        contentDescription = "Leaderboards Button",
                        modifier = modifier
                            .size(buttonSize)
                            .clickable { onLeaderBoardClick() }
                    )
                }

                Spacer(modifier = Modifier.weight(0.5f))
            }
        }

    }
}

/**
 * Preview to inspect the main menu layout in Android Studio
 */
@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    MinovePoleTheme {
        MainMenu(Modifier)
    }
}