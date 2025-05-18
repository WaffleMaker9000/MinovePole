package com.minovepole.logic

import androidx.lifecycle.ViewModel
import com.minovepole.data.DifficultyOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DifficultySelectViewModel : ViewModel() {
    private val _mineDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val mineDifficulty: StateFlow<DifficultyOption> = _mineDifficulty

    private val _sizeDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val sizeDifficulty: StateFlow<DifficultyOption> = _sizeDifficulty

    fun onMineDifficultyChange(option: DifficultyOption) {
        _mineDifficulty.value = option
    }

    fun onSizeDifficultyChange(option: DifficultyOption) {
        _sizeDifficulty.value = option
    }
}