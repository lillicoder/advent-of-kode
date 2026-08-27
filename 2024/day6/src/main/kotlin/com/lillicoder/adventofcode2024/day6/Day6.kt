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

package com.lillicoder.adventofcode2024.day6

import com.lillicoder.adventofcode.kotlin.graphs.SquareLatticeGraph
import com.lillicoder.adventofcode.kotlin.graphs.gridToGraph
import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.math.Direction
import com.lillicoder.adventofcode.kotlin.math.Vertex

fun main() {
    val day6 = Day6()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The number of unique positions visited by the guard is ${day6.part1(input)}.")
    println("The number of valid position to add an obstacle to create a loop is ${day6.part2(input)}.")
}

class Day6 {
    fun part1(input: String): Long {
        val graph = input.gridToGraph()
        val start = graph.find { it.value == "^" }!!
        return graph
            .patrol(start)
            .map { it.vertex }
            .distinct()
            .size
            .toLong()
    }

    fun part2(input: String): Long {
        val graph = input.gridToGraph()
        val start = graph.find { it.value == "^" }!!

        val patrolled = graph.patrol(start)
        return patrolled
            .filter {
                graph
                    .patrol(
                        start,
                        obstruction = it.vertex,
                    ).isCycle()
            }.map {
                it.vertex
            }.distinct()
            .size
            .toLong()
    }

    /**
     * Gets the [Direction] corresponding to making a right hand turn for this direction.
     * @return Direction to the right.
     */
    private fun Direction.turnRight() =
        when (this) {
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT -> Direction.DOWN
            Direction.LEFT_UP -> Direction.RIGHT_UP
            Direction.LEFT_DOWN -> Direction.LEFT_UP
            Direction.RIGHT_UP -> Direction.RIGHT_DOWN
            Direction.RIGHT_DOWN -> Direction.LEFT_DOWN
            else -> this
        }

    /**
     * Determines if this list of [Patrol] represents a cycle.
     * @return True if a cycle, false otherwise.
     */
    private fun List<Patrol<String>>.isCycle() = groupingBy { it }.eachCount().any { it.value > 1 }

    /**
     * Simulates a patrol through this graph from the given starting
     * [Vertex] and starting [Direction].
     * @param start Starting vertex.
     * @param obstacles Vertex values that indicate an obstacle.
     * @param obstruction Optional [Vertex] to consider as obstructed regardless of value.
     * @return Path.
     */
    private fun SquareLatticeGraph<String>.patrol(
        start: Vertex<String>,
        obstacles: Set<String> = setOf("#"),
        obstruction: Vertex<String>? = null,
    ): List<Patrol<String>> {
        val visited = mutableListOf<Patrol<String>>()

        var direction =
            when (start.value) {
                "^" -> Direction.UP
                else -> Direction.UNKNOWN
            }
        var next: Vertex<String>? = start
        while (next != null) {
            val patrol = Patrol(next, direction)
            when (visited.contains(patrol)) {
                true -> return visited.apply { this.add(patrol) } // Cycle, abort
                else -> visited.add(patrol)
            }

            val upcoming = neighbor(next, direction)
            val isObstacle = obstacles.contains(upcoming?.value) || (upcoming != null && upcoming == obstruction)
            when (isObstacle) {
                // Obstacle ahead, go right
                true -> direction = direction.turnRight()
                // No obstacle, continue onward
                false -> next = upcoming
            }
        }

        return visited
    }

    /**
     * Represents a [Vertex] that was patrolled in a [Direction].
     * @param vertex Vertex.
     * @param direction Direction.
     */
    data class Patrol<T>(
        val vertex: Vertex<T>,
        val direction: Direction,
    )
}
