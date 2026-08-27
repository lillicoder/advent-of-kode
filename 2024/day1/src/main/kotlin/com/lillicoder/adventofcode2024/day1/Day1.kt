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

package com.lillicoder.adventofcode2024.day1

import com.lillicoder.adventofcode.kotlin.io.Resources
import kotlin.math.abs

fun main() {
    val day1 = Day1()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The total distance between the lists is ${day1.part1(input)}.")
    println("The similarity score for the lists is ${day1.part2(input)}.")
}

class Day1 {
    fun part1(input: String): Long {
        val (first, second) = input.toLists()
        return first.zip(second).sumOf {
            abs(it.first - it.second)
        }
    }

    fun part2(input: String): Long {
        val (first, second) = input.toLists()
        return first.sumOf { id ->
            second.count { it == id } * id
        }
    }

    /**
     * Converts this string of two numbers separated by space into a
     * [Pair] of sorted lists where the first element contains the first
     * numbers of each line and the second element contains the second
     * numbers of each line.
     */
    private fun String.toLists() =
        lines()
            .map { line ->
                val (first, second) = line.split(" ").filter { it.isNotEmpty() }
                first.toLong() to second.toLong()
            }.unzip()
            .let {
                it.first.sorted() to it.second.sorted()
            }
}
