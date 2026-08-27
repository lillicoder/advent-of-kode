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

package com.lillicoder.adventofcode2024.day2

import com.lillicoder.adventofcode.kotlin.io.Resources
import kotlin.math.abs

fun main() {
    val day2 = Day2()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The number of safe reports is ${day2.part1(input)}.")
    println("The number of safe reports with dampening is ${day2.part2(input)}.")
}

class Day2 {
    fun part1(input: String): Long {
        val reports =
            input.lines().map {
                it.toNumbers()
            }
        return reports
            .count {
                it.isValidReport()
            }.toLong()
    }

    fun part2(input: String): Long {
        val reports =
            input.lines().map {
                it.toNumbers()
            }
        return reports
            .count {
                it.isValidReport(shouldDampen = true)
            }.toLong()
    }

    /**
     * Combines this list with a list of all sub-lists where
     * a single element has been removed.
     * @return Dampened lists.
     */
    private fun List<Long>.dampen() =
        List(size + 1) {
            when (it) {
                size + 1 -> this // Last element should be the original list
                else -> filterIndexed { index, _ -> it != index } // Filter out element at this index
            }
        }

    /**
     * Determines if these numbers represent safe levels for a report.
     * @return True if these levels are safe, false otherwise.
     */
    private fun List<Long>.areSafeLevels(): Boolean {
        if (size <= 1) throw IllegalArgumentException("Cannot determine safety for levels with less than 2 numbers.")

        // Numbers need to be sorted (either low -> high or high -> low)
        // and have a diff between 0 and 4
        val isLowHigh = get(0) <= get(1)
        return windowed(2, 1).all { pair ->
            val (first, second) = pair
            when (isLowHigh) { // order check
                true -> first <= second
                false -> second < first
            } &&
                abs(first - second) in 1..3 // range check
        }
    }

    /**
     * Determines if these numbers represent a valid report.
     * @param shouldDampen True to allow dampening, false otherwise.
     * @return True if this report is valid, false otherwise.
     */
    private fun List<Long>.isValidReport(shouldDampen: Boolean = false): Boolean {
        val reports = if (shouldDampen) dampen() else listOf(this)
        return reports.any { it.areSafeLevels() }
    }

    /**
     * Converts this string of numbers separated by spaces into a list of numbers.
     * @return List of numbers.
     */
    private fun String.toNumbers() = split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
}
