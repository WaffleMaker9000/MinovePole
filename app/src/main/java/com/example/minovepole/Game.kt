package com.example.minovepole

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
    viewModel: GameViewModel,
    x: Int,
    y: Int,
    mineCount: Int
) {
    val time by viewModel.time
    val grid by viewModel.grid

    LaunchedEffect(Unit) {
        viewModel.startGame(x, y, mineCount)
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
        square.isFlagged -> "🚩"
        !square.isClicked -> " "
        square.isMine -> "💣"
        square.number > 0 -> square.number.toString()
        else -> " "
    }

    Box (
        modifier = Modifier
            .border(1.dp, Color.Black)
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures (
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