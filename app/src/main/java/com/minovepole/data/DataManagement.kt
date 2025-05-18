package com.minovepole.data

import android.content.Context
import androidx.annotation.StringRes
import com.example.minovepole.R
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import java.io.File

/**
 * Represents the available difficulty levels in the game
 *
 * Is used to determine both the mine difficulty, which changes the amount of mines,
 * and the size difficulty, which determines the size of the playing field
 */
enum class DifficultyOption(@StringRes val labelResId: Int) {
    /** Easy difficulty - for beginners */
    EASY(R.string.easy_option),
    /** Medium difficulty - balanced */
    MEDIUM(R.string.medium_option),
    /** Hard difficulty - for experts */
    HARD(R.string.hard_option)
}

/**
 * Represents a score entry on the leaderboard
 *
 * @property name Players name
 * @property time The time it took the player to clear the minefield
 * @property mineDifficulty The mine difficulty selected by the player
 * @property sizeDifficulty The size difficulty selected by the player
 */
data class Score (
    val name: String,
    val time: Int,
    val mineDifficulty: DifficultyOption,
    val sizeDifficulty: DifficultyOption
)

/**
 * Represents a single square on the minefield
 *
 * @property isMine Determines whether the square is mined
 * @property isClicked Determines whether the square has already been revealed
 * @property isFlagged Determines whether the player flagged the square
 * @property number Determines the amount of mines in adjacent squares
 */
data class Square (
    var isMine: Boolean = false,
    var isClicked: Boolean = false,
    var isFlagged: Boolean = false,
    var number: Int = 0
)


/**
 * Function used to save a score to local storage, so it can be later displayed in the
 * leaderboard
 *
 * @param context The application context, used to locate save file
 * @param score The score to save
 */
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

/**
 * Function used to read all saved scores from local storage for displaying in the
 * leaderboard
 *
 * @param context The application context, used to locate save file
 */
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

/**
 * Function used to wipe local storage file on user request
 *
 * @param context The application context, used to locate save file
 */
fun clearScores(context: Context) {
    val file = File(context.filesDir, context.getString(R.string.csv))
    if(file.exists()) {
        file.delete()
    }
}

/**
 * Test and demonstration function, creates and saves test scores to local storage,
 * to demonstrate the leaderboard
 *
 * @param context The application context, used to locate save file
 */
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