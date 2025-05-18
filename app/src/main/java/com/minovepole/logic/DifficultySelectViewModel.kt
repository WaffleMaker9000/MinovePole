package com.minovepole.logic

import androidx.lifecycle.ViewModel
import com.minovepole.data.DifficultyOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * ViewModel to manage the state of the difficulty select buttons on the
 * difficulty select screen
 *
 * Handles both mine difficulty and size difficulty
 */
class DifficultySelectViewModel : ViewModel() {
    // Mine difficulty state tracking
    private val _mineDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val mineDifficulty: StateFlow<DifficultyOption> = _mineDifficulty

    // Size difficulty state tracking
    private val _sizeDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val sizeDifficulty: StateFlow<DifficultyOption> = _sizeDifficulty

    /**
     * Updates currently selected mine difficulty
     *
     * @param option Newly selected mine difficulty
     */
    fun onMineDifficultyChange(option: DifficultyOption) {
        _mineDifficulty.value = option
    }

    /**
     * Updates currently selected size difficulty
     *
     * @param option Newly selected size difficulty
     */
    fun onSizeDifficultyChange(option: DifficultyOption) {
        _sizeDifficulty.value = option
    }
}