package com.minovepole.logic

import androidx.lifecycle.ViewModel
import com.minovepole.data.DifficultyOption
import com.minovepole.data.Score
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for managing the state of the LeaderBoard screen
 *
 * This ViewModel handles:
 * - The currently selected combination of difficulties to filter by
 * - All recorded scores
 */
class LeaderBoardViewModel : ViewModel() {
    // Mine difficulty state tracking
    private val _mineDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val mineDifficulty: StateFlow<DifficultyOption> = _mineDifficulty

    // Size difficulty state tracking
    private val _sizeDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val sizeDifficulty: StateFlow<DifficultyOption> = _sizeDifficulty

    // State holding all loaded scores
    private val _allScores = MutableStateFlow<List<Score>>(emptyList())
    val allScores: StateFlow<List<Score>> = _allScores.asStateFlow()

    /**
     * Called on screen composition, to update allScores with loaded data
     *
     * @param scores Loaded scores to update with
     */
    fun setScores(scores: List<Score>) {
        _allScores.value = scores
    }

    /**
     * Clears currently loaded scores
     */
    fun clearScores() {
        _allScores.value = emptyList()
    }

    /**
     * Updates currently selected mine difficulty
     *
     * @param option Newly selected mine difficulty
     */
    fun onMineDifficultyChanged(option: DifficultyOption) {
        _mineDifficulty.value = option
    }

    /**
     * Updates currently selected size difficulty
     *
     * @param option Newly selected size difficulty
     */
    fun onSizeDifficultyChanged(option: DifficultyOption) {
        _sizeDifficulty.value = option
    }
}