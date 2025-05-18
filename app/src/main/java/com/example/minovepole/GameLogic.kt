package com.example.minovepole

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

fun generateGrid(x: Int, y: Int, count: Int) : List<List<Square>> {
    val grid = List(x) { List(y) { Square() } }
    generateMines(grid, count)
    calculateSurrounding(grid)
    return grid
}



fun revealSquares(original: List<List<Square>>, x: Int, y: Int): List<List<Square>> {
    if (x !in original.indices || y !in original[0].indices) return original
    if (original[x][y].isClicked || original[x][y].isFlagged) return original
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