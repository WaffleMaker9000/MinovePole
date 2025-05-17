package com.example.minovepole

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _time = mutableIntStateOf(0)
    val time: State<Int> = _time
    private var timerJob: Job? = null

    private val _grid = mutableStateOf<List<List<Square>>>(emptyList())
    val grid: State<List<List<Square>>> = _grid

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while(isActive) {
                delay(1000L)
                _time.intValue += 1
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun startGame(x: Int, y: Int, mineCount: Int) {
        _grid.value = generateGrid(x, y, mineCount)
        _time.intValue = 0
        startTimer()
    }

    private fun revealSquares(original: List<List<Square>>, x: Int, y: Int): List<List<Square>> {
        if (x !in original.indices || y !in original[0].indices) return original
        if (original[x][y].isClicked) return original
        var updatedGrid = original.mapIndexed { currX, col ->
            col.mapIndexed{ currY, square ->
                if (currX == x && currY == y) square.copy(isClicked = true) else square
            }
        }

        if (updatedGrid[x][y].number > 0) return updatedGrid

        for (adjX in -1..1) {
            for (adjY in -1..1) {
                if (adjX == 0 && adjY == 0) continue
                updatedGrid = revealSquares(updatedGrid, x + adjX, y + adjY)
            }
        }

        return updatedGrid
    }

    fun onSquareClick(x: Int, y: Int) {
        _grid.value = revealSquares(grid.value, x, y)
    }

    fun onClickHold(x: Int, y: Int) {
        val updatedGrid = _grid.value.mapIndexed { currX, col ->
            col.mapIndexed{ currY, square ->
                if (currX == x && currY == y) square.copy(isFlagged = !square.isFlagged) else square
            }
        }
        _grid.value = updatedGrid
    }
}