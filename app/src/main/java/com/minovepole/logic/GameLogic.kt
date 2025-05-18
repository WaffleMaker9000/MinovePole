package com.minovepole.logic

import com.minovepole.data.Square


/**
 * Function used to randomly generate a given number of mines on a minefield
 *
 * @param grid The minefield to generate mines on
 * @param count The amount of mines to generate
 */
fun generateMines(grid: List<List<Square>>, count: Int) {
    val x = grid.size
    val y = grid[0].size

    var placed = 0

    while (placed < count) {
        val mineX = (0 until x).random()
        val mineY = (0 until y).random()

        if (!grid[mineX][mineY].isMine) {
            grid[mineX][mineY].isMine = true
            placed++
        }
    }
}


/**
 * Function used to initialise the adjacent mine numbers in each square,
 * after the mines have been generated
 *
 * @param grid Minefield to process
 */
fun calculateSurrounding(grid: List<List<Square>>) {
    val x = grid.size
    val y = grid[0].size

    for (currX in 0 until x){
        for(currY in 0 until y) {
            var number = 0

            for (adjX in -1..1) {
                for (adjY in -1..1) {
                    if (adjX == 0 && adjY == 0) continue

                    val checkX = adjX + currX
                    val checkY = adjY + currY

                    if (
                        checkX in 0 until x &&
                        checkY in 0 until y &&
                        grid[checkX][checkY].isMine
                        ) number++
                }
            }

            grid[currX][currY].number = number
        }
    }
}

/**
 * Function used to generate a minefield from scratch along with the mines
 *
 * @param x The number of columns in the minefield
 * @param y The number of rows in the minefield
 * @param count the number of mines to generate
 * @return Returns generated miefield
 */
fun generateGrid(x: Int, y: Int, count: Int) : List<List<Square>> {
    val grid = List(x) { List(y) { Square() } }
    generateMines(grid, count)
    calculateSurrounding(grid)
    return grid
}

/**
 * Recursive function to reveal squares when they've been clicked. Uncovers itself, and if
 * there are no mines around it, also uncovers all adjacent tiles
 *
 * @param original The input minefield
 * @param x Column of current square
 * @param y Row of current square
 * @return Returns new minefield with uncovered squares
 */
fun revealSquares(original: List<List<Square>>, x: Int, y: Int): List<List<Square>> {
    // Terminate recursion before uncovering self if x and y are out of bounds
    if (x !in original.indices || y !in original[0].indices) return original
    // Terminate recursion before uncovering self if the square is flagged or already uncovered
    if (original[x][y].isClicked || original[x][y].isFlagged) return original
    // Have to copy and reassign the whole 2d list, so that its updated in composables
    var updatedGrid = original.mapIndexed { currX, col ->
        col.mapIndexed{ currY, square ->
            if (currX == x && currY == y) square.copy(isClicked = true) else square
        }
    }

    // Terminate recursion after uncovering self if there are mines around
    if (updatedGrid[x][y].number > 0) return updatedGrid

    // Uncover every adjacent square
    for (adjX in -1..1) {
        for (adjY in -1..1) {
            if (adjX == 0 && adjY == 0) continue
            updatedGrid = revealSquares(updatedGrid, x + adjX, y + adjY)
        }
    }

    return updatedGrid
}