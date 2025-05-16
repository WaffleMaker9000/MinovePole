package com.example.minovepole

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import java.io.File


enum class DifficultyOption(@StringRes val labelResId: Int) {
    EASY(R.string.easy_option),
    MEDIUM(R.string.medium_option),
    HARD(R.string.hard_option)
}


data class Score (
    val name: String,
    val time: Int,
    val mineDifficulty: DifficultyOption,
    val sizeDifficulty: DifficultyOption
)

fun saveScore(context: Context, score: Score) {
    val file = File(context.filesDir, context.getString(R.string.csv))
    val csvWriter = CsvWriter()

    csvWriter.open(file, append = true) {
        writeRow(listOf(
            score.name,
            score.time.toString(),
            score.mineDifficulty.ordinal.toString(),
            score.sizeDifficulty.ordinal.toString()
        ))
    }
}

fun readScores(context: Context): List<Score> {
    val file = File(context.filesDir, context.getString(R.string.csv))
    if (!file.exists() || file.length() == 0L) return emptyList()
    return CsvReader().readAll(file).mapNotNull { score ->
        val name = score.getOrNull(0)
        val time = score.getOrNull(1)
        val mineDifficulty = score.getOrNull(2)
        val sizeDifficulty = score.getOrNull(3)
        if (name != null && time != null && mineDifficulty != null && sizeDifficulty != null)
            Score(
                name,
                time.toInt(),
                DifficultyOption.entries[mineDifficulty.toInt()],
                DifficultyOption.entries[sizeDifficulty.toInt()]
            ) else null
    }.sortedBy { it.time }
}

fun generateTestScores(context: Context) {
    val testScores = listOf(
        Score("Gobsmasha", 20, DifficultyOption.EASY, DifficultyOption.EASY),
        Score("Grotsnik", 40, DifficultyOption.EASY, DifficultyOption.EASY),
        Score("Wazzdakka", 35, DifficultyOption.MEDIUM, DifficultyOption.HARD),
        Score("Big Mek", 50, DifficultyOption.HARD, DifficultyOption.HARD),
        Score("Waffle", 10, DifficultyOption.MEDIUM, DifficultyOption.MEDIUM)
    )

    for (score in testScores) {
        saveScore(context, score)
    }
}