package com.minovepole.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.minovepole.R
import com.minovepole.logic.GameViewModel
import com.minovepole.sound.SoundManager
import com.minovepole.data.Square

/**
 * Preview composable that is used to inspect the layout in Android Studio
 *
 * Provides the GridUI composable a mock minefield to display.
 *
 * This is technically duplicate code, but the Game composable needs a viewmodel, and this was
 * the simplest way i could think of to preview it
 */
@Preview (showBackground = true)
@Composable
fun GamePreview () {
    val sampleGrid = List(6) { x ->
        List(12) { y ->
            Square(
                isClicked = false,
                isMine = (x + y) % 7 == 0,
                isFlagged = false,
                number = (x + y) % 3
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(35.dp))
            Text(
                text = stringResource(R.string.timer),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = 5.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            GridUI(
                grid = sampleGrid,
                onSquareClick = { _, _ -> },
                onClickHold = { _, _ -> },
            )
        }
    }
}


/**
 * Composable that displays the game screen, with a timer, the minefield, and popups for
 * winning and losing
 *
 * @param sizeDiff Size difficulty selected in the difficulty select screen
 * @param mineDiff Mine difficulty selected in the difficulty select screen
 * @param viewModel The game's view model, tracks the game state
 * @param onMenuClick Callback that is called when the menu button is clicked,
 *  * sends player to the menu screen
 * @param onLeaderBoardClick Callback that is called when the save button is clicked,
 * sends player to the leaderboards screen
 */
@Composable
fun Game (
    sizeDiff: Int,
    mineDiff: Int,
    viewModel: GameViewModel,
    onMenuClick: () -> Unit,
    onLeaderBoardClick: () -> Unit
) {
    // State trackers
    val time by viewModel.time
    val grid by viewModel.grid
    val isWin by viewModel.isWin
    val isGameOver by viewModel.isGameOver

    // Generate the minefield and start the game before composition
    LaunchedEffect(Unit) {
        viewModel.startGame(
            mineDiff = mineDiff,
            sizeDiff =  sizeDiff
        )
    }

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(35.dp))
            // Time display
            Text(
                text = stringResource(R.string.timer),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = time.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Displays minefield, if there is a minefield. The condition prevents crashing
            if (grid.isNotEmpty() && grid[0].isNotEmpty()) {
                GridUI(
                    grid = grid,
                    onSquareClick = {clickX, clickY -> viewModel.onSquareClick(clickX, clickY)},
                    onClickHold = {clickX, clickY -> viewModel.onClickHold(clickX, clickY)}
                )
            }
        }
    }

    // Displays the win pop up
    if (isWin) {
        WinPopUp(
            onMenuClick = onMenuClick,
            onSaveClick = onLeaderBoardClick,
            viewModel = viewModel
        )
    }

    // Displays the lose popup
    if (isGameOver && !isWin) {
        LosePopUp(
            onMenuClick = onMenuClick,
            onRetryClick =  {
                viewModel.restartGame(
                    mineDiff = mineDiff,
                    sizeDiff = sizeDiff
                )
            }
        )
    }
}

/**
 * Composable that pops up when a mine is blown up
 *
 * Plays the explosion sound
 *
 * @param onMenuClick Callback that is called when the menu button is clicked
 * @param onRetryClick Callback that is called when the retry button is clicked
 */
@Composable
fun LosePopUp(
    onMenuClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    // Play explosion sound
    SoundManager.playExplosionSound()

    Dialog(
        // Undismissable
        onDismissRequest = {}
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.LightGray
        ) {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = stringResource(R.string.lose),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Menu button
                    Button(
                        onClick = onMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text(
                            text = stringResource(R.string.menu),
                            color = Color.White
                        )
                    }

                    // Retry button
                    Button(
                        onClick = onRetryClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(
                            text = stringResource(R.string.retry),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable that pops up when the win condition is met
 *
 * Plays the win sound
 *
 * @param onMenuClick Callback that is called when the menu button is clicked
 * @param onSaveClick Callback that is called when the save button is clicked
 * @param viewModel The game's view model, needed to call the save score function
 */
@Composable
fun WinPopUp(
    onMenuClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: GameViewModel
) {
    // Name text field state tracker
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Play win sound
    SoundManager.playWinSound()

    Dialog(
        // Undismissable
        onDismissRequest = {}
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.LightGray,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = stringResource(R.string.win),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Yellow
                )

                // Name entry field, for saving score
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.name)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        unfocusedContainerColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.White
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Menu button
                    Button(
                        onClick = onMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text(
                            text = stringResource(R.string.menu),
                            color = Color.White
                        )
                    }

                    // Save button
                    Button(
                        onClick = {
                            // Save score first
                            viewModel.onSaveScoreClick(context, name)
                            // Then call the callback
                            onSaveClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(
                            text = stringResource(R.string.save_score),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable that displays the whole minefiled, made up of squares
 *
 * @param grid The minefield to display
 * @param onSquareClick Callback that is called when the square is clicked
 * @param onClickHold Callback that is called when the square is long clicked
 */
@Composable
fun GridUI(
    grid: List<List<Square>>,
    onSquareClick: (clickX: Int, clickY: Int) -> Unit,
    onClickHold: (clickX: Int, clickY: Int) -> Unit
) {
    BoxWithConstraints (
        modifier = Modifier.padding(35.dp)
    ){
        // Calculate how big the squares need to be to fill the screen horizontally
        val squareSize = minOf(maxWidth, maxHeight) / minOf(grid.size, grid[0].size)

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // For each square in the minefield, displays a square Composable
            grid.indices.forEach { x ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    grid[0].indices.forEach{ y ->
                        SquareUI(
                            square = grid[x][y],
                            onClick = { onSquareClick(x, y) },
                            onClickHold = { onClickHold(x, y) },
                            size = squareSize
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable representing a single square in the minefield
 *
 * @param square Data class to draw from
 * @param onClick Callback that is called when the square is clicked
 * @param onClickHold Callback that is called when the square is long clicked
 * @param size The size of the square in dp
 */
@Composable
fun SquareUI(
    square: Square,
    onClick: () -> Unit,
    onClickHold: () -> Unit,
    size: Dp
) {
    val backgroundColor = when {
        square.isClicked && square.isMine -> Color.Red
        square.isClicked -> Color.Gray
        else -> Color.DarkGray
    }

    val text = when {
        square.isFlagged -> stringResource(R.string.flag)
        !square.isClicked -> stringResource(R.string.empty)
        square.isMine -> stringResource(R.string.bomb)
        square.number > 0 -> square.number.toString()
        else -> stringResource(R.string.empty)
    }

    Box (
        modifier = Modifier
            .border(1.dp, Color.Black)
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onClickHold() },
                    onTap = { onClick() }
                )
            }
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Text( text = text )
    }
}