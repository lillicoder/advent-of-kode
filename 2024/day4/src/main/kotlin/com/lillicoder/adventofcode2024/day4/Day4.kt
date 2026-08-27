/*
 * Copyright 2026 Scott Weeden-Moody
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this project except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lillicoder.adventofcode2024.day4

import com.lillicoder.adventofcode.kotlin.grids.Grid
import com.lillicoder.adventofcode.kotlin.grids.toGrid
import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.math.Direction
import com.lillicoder.adventofcode.kotlin.math.Vertex
import com.lillicoder.adventofcode.kotlin.text.splitNotEmpty

fun main() {
    val day4 = Day4()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The number of XMAS in the grid is ${day4.part1(input)}.")
    println("The number of X-MAS in the grid is ${day4.part2(input)}.")
}

class Day4 {
    fun part1(input: String): Long {
        val token = "XMAS"
        val grid = input.toGrid { it.toString() }
        return grid
            .filter {
                token.startsWith(it.value)
            }.sumOf {
                grid.wordSearch(it, token)
            }
    }

    fun part2(input: String): Long {
        val token = "MAS"
        val start = token.first().toString()
        val end = token.last().toString()
        val grid = input.toGrid { it.toString() }
        return grid
            .filter {
                // Only check vertices that start or end of the desired token
                it.value == start || it.value == end
            }.count {
                grid.xSearch(it, token)
            }.toLong()
    }

    /**
     * Performs a word search for the given token starting
     * at the given [Vertex] in all directions.
     * @param start Starting vertex.
     * @param token Token to find.
     * @return Number of times token appears from the starting vertex.
     */
    private fun Grid<String>.wordSearch(
        start: Vertex<String>,
        token: String,
    ): Long {
        val directions = Direction.entries.filterNot { it == Direction.UNKNOWN }
        return directions.count { wordSearch(start, token, it) }.toLong()
    }

    /**
     * Performs a word search for the given token starting
     * at the given [Vertex] in the given [Direction].
     * @param start Starting vertex.
     * @param token Token to find.
     * @param direction Direction to search.
     * @return True if the token was found, false otherwise.
     */
    private fun Grid<String>.wordSearch(
        start: Vertex<String>,
        token: String,
        direction: Direction,
    ): Boolean {
        val characters = token.splitNotEmpty("").iterator()

        var next: Vertex<String>? = start
        while (next != null && characters.hasNext()) {
            if (characters.next() != next.value) return false
            next = neighbor(next, direction)
        }

        return !characters.hasNext() // If there are any left we aborted due to null neighbors
    }

    /**
     * Performs an x-search for the given token starting at the given [Vertex]. An
     * x-search only checks diagonal directions for an occurrence of a token, with
     * the given vertex serving as the center character of the token to find.
     * @param start Starting vertex.
     * @param token Token to find.
     * @return Number of times token appears from the starting vertex.
     */
    private fun Grid<String>.xSearch(
        start: Vertex<String>,
        token: String,
        reversed: String = token.reversed(),
    ): Boolean {
        // Check first diagonal
        val isStartMatch =
            wordSearch(
                start,
                token,
                Direction.RIGHT_DOWN,
            )
        val isStartMatchReversed =
            wordSearch(
                start,
                reversed,
                Direction.RIGHT_DOWN,
            )
        if (!isStartMatch && !isStartMatchReversed) return false

        // Check second diagonal
        val coordinates = coordinates(start)
        val other =
            coordinates
                ?.copy(
                    x = coordinates.x + (token.length - 1),
                )?.let {
                    vertex(it)
                }

        if (other == null) return false

        val isOtherMatch =
            wordSearch(
                other,
                token,
                Direction.LEFT_DOWN,
            )
        val isOtherMatchReversed =
            wordSearch(
                other,
                reversed,
                Direction.LEFT_DOWN,
            )
        return isOtherMatch || isOtherMatchReversed
    }
}
