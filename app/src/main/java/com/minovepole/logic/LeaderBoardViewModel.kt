package com.minovepole.logic

import androidx.lifecycle.ViewModel
import com.minovepole.data.DifficultyOption
import com.minovepole.data.Score
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LeaderBoardViewModel : ViewModel() {
    private val _mineDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val mineDifficulty: StateFlow<DifficultyOption> = _mineDifficulty

    private val _sizeDifficulty = MutableStateFlow(DifficultyOption.MEDIUM)
    val sizeDifficulty: StateFlow<DifficultyOption> = _sizeDifficulty

    private val _allScores = MutableStateFlow<List<Score>>(emptyList())
    val allScores: StateFlow<List<Score>> = _allScores.asStateFlow()

    fun setScores(scores: List<Score>) {
        _allScores.value = scores
    }

    fun clearScores() {
        _allScores.value = emptyList()
    }

    fun onMineDifficultyChanged(option: DifficultyOption) {
        _mineDifficulty.value = option
    }

    fun onSizeDifficultyChanged(option: DifficultyOption) {
        _sizeDifficulty.value = option
    }
}