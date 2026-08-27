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

package com.lillicoder.adventofcode2024.day8

import com.lillicoder.adventofcode.kotlin.grids.Grid
import com.lillicoder.adventofcode.kotlin.grids.toGrid
import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.math.Coordinates

fun main() {
    val day8 = Day8()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The number of unique locations in the map with an antinode is ${day8.part1(input)}.")
    println("The number of unique locations in the map with repeated antinodes is ${day8.part2(input)}.")
}

class Day8 {
    fun part1(input: String): Long {
        val grid = input.toGrid()
        val coordinatesByAntennae = grid.antennaClusters()

        return coordinatesByAntennae
            .flatMap { entry ->
                entry.value.pairOff().flatMap {
                    grid.antiNodes(it.first, it.second, 1, false)
                }
            }.distinct()
            .size
            .toLong()
    }

    fun part2(input: String): Long {
        val grid = input.toGrid()
        val coordinatesByAntennae = grid.antennaClusters()

        return coordinatesByAntennae
            .flatMap { entry ->
                entry.value.pairOff().flatMap {
                    grid.antiNodes(it.first, it.second)
                }
            }.distinct()
            .size
            .toLong()
    }

    /**
     * Subtracts these coordinates from the given [Coordinates].
     * @param other Coordinates.
     * @return Difference.
     */
    private fun Coordinates.minus(other: Coordinates) =
        copy(
            x = x - other.x,
            y = y - other.y,
        )

    /**
     * Adds these coordinates to the given [Coordinates].
     * @param other Coordinates.
     * @return Sum.
     */
    private fun Coordinates.plus(other: Coordinates) =
        copy(
            x = x + other.x,
            y = y + other.y,
        )

    /**
     * Gets a map of all distinct antenna [Coordinates] keyed by antenna type.
     * @return Coordinates by antenna.
     */
    private fun Grid<String>.antennaClusters() =
        filterNot {
            it.value == "."
        }.groupBy(
            {
                it.value
            },
            {
                coordinates(it)
            },
        ).mapValues {
            it.value.filterNotNull()
        }

    /**
     * Gets the [Coordinates] for the eligible antinodes for the given [Coordinates].
     * @param first First coordinates.
     * @param second Second coordinates.
     * @param limit Maximum number of antinodes to consider.
     * @return Antinode coordinates.
     */
    private fun Grid<String>.antiNodes(
        first: Coordinates,
        second: Coordinates,
        limit: Int = Int.MAX_VALUE,
        includeStartingCoordinates: Boolean = true,
    ): List<Coordinates> {
        val coordinates =
            when (includeStartingCoordinates) {
                true -> mutableListOf(first, second)
                else -> mutableListOf()
            }

        val offset = first.minus(second)
        var next = first.plus(offset)
        while (contains(next) && coordinates.size < limit) {
            coordinates.add(next)
            next = next.plus(offset)
        }

        return coordinates
    }

    /**
     * Determines if this grid contains a vertex at the given [Coordinates].
     * @param coordinates Coordinates.
     * @return True if coordinates are in-bounds, false otherwise.
     */
    private fun Grid<String>.contains(coordinates: Coordinates) = coordinates.x in 0..<width && coordinates.y in 0..<height

    /**
     * Creates a list of all elements where each element is paired with
     * every other.
     * @return Paired elements.
     */
    private fun <T> List<T>.pairOff() =
        flatMapIndexed { index, _ ->
            pairOff(index)
        }.distinct()

    /**
     * Pairs the element at the given index with all other elements in this list.
     * @index Index.
     * @return Paired elements.
     */
    private fun <T> List<T>.pairOff(index: Int) =
        get(index).let { element ->
            mapIndexedNotNull { i, next ->
                if (index == i) null else element to next
            }
        }
}
