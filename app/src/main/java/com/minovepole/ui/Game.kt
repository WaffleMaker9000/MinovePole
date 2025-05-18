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

@Composable
fun Game (
    sizeDiff: Int,
    mineDiff: Int,
    viewModel: GameViewModel,
    onMenuClick: () -> Unit,
    onLeaderBoardClick: () -> Unit
) {
    val time by viewModel.time
    val grid by viewModel.grid
    val isWin by viewModel.isWin
    val isGameOver by viewModel.isGameOver

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
            if (grid.isNotEmpty() && grid[0].isNotEmpty()) {
                GridUI(
                    grid = grid,
                    onSquareClick = {clickX, clickY -> viewModel.onSquareClick(clickX, clickY)},
                    onClickHold = {clickX, clickY -> viewModel.onClickHold(clickX, clickY)}
                )
            }
        }
    }
    if (isWin) {
        WinPopUp(
            onMenuClick = onMenuClick,
            onSaveClick = onLeaderBoardClick,
            viewModel = viewModel
        )
    }
    if (isGameOver && !isWin) {
        LosePopUp(
            onMenuClick = onMenuClick,
            onRetryClick =  {
                viewModel.startGame(
                    mineDiff = mineDiff,
                    sizeDiff = sizeDiff
                )
            }
        )
    }
}

@Composable
fun LosePopUp(
    onMenuClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    SoundManager.playExplosionSound()

    Dialog(
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
                    Button(
                        onClick = onMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text(
                            text = stringResource(R.string.menu),
                            color = Color.White
                        )
                    }
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

@Composable
fun WinPopUp(
    onMenuClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: GameViewModel
) {
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    SoundManager.playWinSound()

    Dialog(
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
                Text(
                    text = stringResource(R.string.win),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Yellow
                )
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
                    Button(
                        onClick = onMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text(
                            text = stringResource(R.string.menu),
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.saveScore(context, name)
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

@Composable
fun GridUI(
    grid: List<List<Square>>,
    onSquareClick: (clickX: Int, clickY: Int) -> Unit,
    onClickHold: (clickX: Int, clickY: Int) -> Unit
) {
    BoxWithConstraints (
        modifier = Modifier.padding(35.dp)
    ){
        val squareSize = minOf(maxWidth, maxHeight) / minOf(grid.size, grid[0].size)

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
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