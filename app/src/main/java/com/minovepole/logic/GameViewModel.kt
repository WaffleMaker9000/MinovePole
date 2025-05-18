package com.minovepole.logic

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minovepole.data.DifficultyOption
import com.minovepole.data.Score
import com.minovepole.data.Square
import com.minovepole.data.saveScore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the game state in the Minesweeper game.
 *
 * This ViewModel handles:
 * - Timer tracking during gameplay
 * - Revealed cell logic and win/loss conditions
 * - Exposing game data to the UI via StateFlow
 */
class GameViewModel : ViewModel() {
    // Timer state tracking
    private val _time = mutableIntStateOf(0)
    val time: State<Int> = _time
    private var timerJob: Job? = null

    // Used for retry and to avoid needless recomposition
    private var _isStarted = false

    // Minefield state tracking
    private val _grid = mutableStateOf<List<List<Square>>>(emptyList())
    val grid: State<List<List<Square>>> = _grid

    // Game in progress tracking
    private val _isGameOver = mutableStateOf(false)
    val isGameOver: State<Boolean> = _isGameOver

    // Victory condition tracking
    private val _isWin = mutableStateOf(false)
    val isWin: State<Boolean> = _isWin

    private var _mineDiff = 0
    private var _sizeDiff = 0

    /**
     * Starts the game timer
     */
    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while(isActive) {
                delay(1000L)
                _time.intValue += 1
            }
        }
    }

    /**
     * Stops the game timer
     */
    fun stopTimer() {
        timerJob?.cancel()
    }

    /**
     * Function for performing tasks needed to start a game
     *
     * @param mineDiff Mine difficulty selected
     * @param sizeDiff Size difficulty selected
     */
    fun startGame(mineDiff: Int, sizeDiff: Int) {
        // Safeguard against recomposition, e.g. when rotated
        if (_isStarted) return
        _isStarted = true

        _mineDiff = mineDiff
        _sizeDiff = sizeDiff

        // X size calculation, default 4, adding 2 for each size difficulty higher than easy
        val x = 4 + (sizeDiff * 2)
        // Y size is double the X size
        val y = x * 2
        // Mine count calculation, default 6,
        // adding 2 for each size and mine difficulty higher than easy
        val mineCount = 6 + ((mineDiff + sizeDiff) * 2)
        _grid.value = generateGrid(x, y, mineCount)

        // Reset timer and game over tracking
        _time.intValue = 0
        _isGameOver.value = false
        _isWin.value = false

        startTimer()
    }

    /**
     * Function that determines what happens when the player clicks a square
     *
     * Handles revealing squares, and checking win and lose conditions
     *
     * @param x Column of clicked square
     * @param y Row of clicked square
     */
    fun onSquareClick(x: Int, y: Int) {
        // Do nothing if the square is flagged or the game is over
        if (_grid.value[x][y].isFlagged || isGameOver.value) return
        // Reveal squares
        _grid.value = revealSquares(grid.value, x, y)
        // Game over catch
        if (_grid.value[x][y].isMine) {
            _isGameOver.value = true
            stopTimer()
            return
        }
        // Win condition check
        _grid.value.forEach{ col ->
            col.forEach{ square ->
                if (!square.isMine && !square.isClicked) return
            }
        }
        _isGameOver.value = true
        _isWin.value = true
        stopTimer()
    }

    /**
     * Function that determines what happens on long pressing a square
     *
     * Handles flagging and unflagging of squares
     *
     * @param x Column of clicked square
     * @param y Row of clicked square
     */
    fun onClickHold(x: Int, y: Int) {
        // Cant flag if its already clicked
        if (_grid.value[x][y].isClicked) return
        // Again, have to fully copy and reassign the grid, because of composables
        val updatedGrid = _grid.value.mapIndexed { currX, col ->
            col.mapIndexed{ currY, square ->
                if (currX == x && currY == y) square.copy(isFlagged = !square.isFlagged) else square
            }
        }
        _grid.value = updatedGrid
    }

    /**
     * Function that saves the player's score after pressing the save button
     *
     * @param context Application context, the writing function needs it
     * @param name The players name
     */
    fun onSaveScoreClick(context: Context, name: String) {
        val score = Score(
            name = name,
            time = time.value,
            mineDifficulty = DifficultyOption.entries[_mineDiff],
            sizeDifficulty = DifficultyOption.entries[_sizeDiff]
        )
        saveScore(context = context, score = score)
    }

    /**
     * Enables recomposition for restarting the game, and then generates a new minefield
     *
     * @param mineDiff Mine difficulty selected
     * @param sizeDiff Size difficulty selected
     */
    fun restartGame(mineDiff: Int, sizeDiff: Int) {
        _isStarted = false
        startGame(mineDiff, sizeDiff)
    }
}