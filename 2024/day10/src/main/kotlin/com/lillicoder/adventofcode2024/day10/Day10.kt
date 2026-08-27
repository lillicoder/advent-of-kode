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

package com.lillicoder.adventofcode2024.day10

import com.lillicoder.adventofcode.kotlin.graphs.Graph
import com.lillicoder.adventofcode.kotlin.graphs.graph
import com.lillicoder.adventofcode.kotlin.graphs.traversal.DepthFirstTraversal
import com.lillicoder.adventofcode.kotlin.grids.toGrid
import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.math.Vertex

fun main() {
    val day10 = Day10()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The sum of all trailhead scores is ${day10.part1(input)}.")
    println("The sum of all trailhead ratings is ${day10.part2(input)}.")
}

class Day10 {
    fun part1(input: String): Long {
        val grid = input.toGrid { it.digitToInt() }
        val graph =
            graph<Int> {
                // Copy the vertices
                grid.forEach {
                    vertex(it)
                }

                // Make edges only for each neighbor whose
                // value is 1 away
                grid.forEach { vertex ->
                    grid
                        .neighbors(vertex)
                        .filter {
                            it.value - vertex.value == 1
                        }.forEach { neighbor ->
                            edge(
                                sourceId = vertex.id,
                                destinationId = neighbor.id,
                            ) {
                                directed()
                            }
                        }
                }
            }

        // Graph now has directed paths for each 0-value vertex;
        // count all encounters with 9-value vertices when getting
        // all paths from the 0-value vertices
        return graph
            .filter { it.value == 0 }
            .sumOf { vertex ->
                DepthFirstTraversal(graph).path(vertex).count { it.value == 9 }
            }.toLong()
    }

    fun part2(input: String): Long {
        val grid = input.toGrid { it.digitToInt() }
        val graph =
            graph<Int> {
                // Copy the vertices
                grid.forEach {
                    vertex(it)
                }

                // Make edges only for each neighbor whose
                // value is 1 away
                grid.forEach { vertex ->
                    grid
                        .neighbors(vertex)
                        .filter {
                            it.value - vertex.value == 1
                        }.forEach { neighbor ->
                            edge(
                                sourceId = vertex.id,
                                destinationId = neighbor.id,
                            ) {
                                directed()
                            }
                        }
                }
            }

        return graph
            .filter { it.value == 0 }
            .sumOf { vertex ->
                graph.countPathsFrom(vertex) { it == 9 }
            }.toLong()
    }
}

/**
 * Counts the number of vertices in this graph that satisfy the given predicate when
 * finding paths from the given [Vertex].
 *
 * Note: This method does not handle graphs with cycles. Use only with directed acyclic graphs (DAGs).
 * @param start Starting vertex.
 * @param predicate Predicate to check
 * @return Number of vertices in found paths that satisfy the given predicate.
 */
private fun <T> Graph<T>.countPathsFrom(
    start: Vertex<T>,
    predicate: (T) -> Boolean,
): Int {
    var count = 0
    val stack = ArrayDeque<Vertex<T>>().also { it.add(start) }

    while (stack.isNotEmpty()) {
        val current = stack.removeFirst()
        if (predicate(current.value)) {
            count += 1
        } else {
            neighbors(current).forEach {
                stack.addFirst(it)
            }
        }
    }

    return count
}
