package com.example.minovepole

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
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

    private val _isGameOver = mutableStateOf(false)
    val isGameOver: State<Boolean> = _isGameOver

    private val _isWin = mutableStateOf(false)
    val isWin: State<Boolean> = _isWin

    private var _mineDiff = 0
    private var _sizeDiff = 0

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

    fun startGame(mineDiff: Int, sizeDiff: Int) {
        _mineDiff = mineDiff
        _sizeDiff = sizeDiff

        val x = 4 + (sizeDiff * 2)
        val y = x * 2
        val mineCount = 6 + ((mineDiff + sizeDiff) * 2)
        _grid.value = generateGrid(x, y, mineCount)

        _time.intValue = 0
        _isGameOver.value = false
        _isWin.value = false

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
        if (_grid.value[x][y].isFlagged || isGameOver.value) return
        _grid.value = revealSquares(grid.value, x, y)
        if (_grid.value[x][y].isMine) {
            _isGameOver.value = true
            stopTimer()
            return
        }
        _grid.value.forEach{ col ->
            col.forEach{ square ->
                if (!square.isMine && !square.isClicked) return
            }
        }
        _isGameOver.value = true
        _isWin.value = true
        stopTimer()
    }

    fun onClickHold(x: Int, y: Int) {
        val updatedGrid = _grid.value.mapIndexed { currX, col ->
            col.mapIndexed{ currY, square ->
                if (currX == x && currY == y) square.copy(isFlagged = !square.isFlagged) else square
            }
        }
        _grid.value = updatedGrid
    }

    fun saveScore(context: Context, name: String) {
        val score = Score(
            name = name,
            time = time.value,
            mineDifficulty = DifficultyOption.entries[_mineDiff],
            sizeDifficulty = DifficultyOption.entries[_sizeDiff]
        )
        com.example.minovepole.saveScore(context = context, score = score)
    }
}