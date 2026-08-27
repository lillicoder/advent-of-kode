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

package com.lillicoder.adventofcode2024.day5

import com.lillicoder.adventofcode.kotlin.io.Resources

fun main() {
    val day5 = Day5()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The sum of middle page numbers for correctly ordered updates is ${day5.part1(input)}.")
    println("The sum of middle page numbers for corrected updates is ${day5.part2(input)}.")
}

class Day5 {
    fun part1(input: String): Long {
        val sections = input.toSections()

        // First section is page precedence rules
        val precedences =
            sections[0]
                .lines()
                .map {
                    it.split("|")
                }.groupBy(
                    { it.first() },
                    { it.last() },
                )

        // Second section is page orders
        return sections[1]
            .lines()
            .map {
                it.split(",")
            }.filter {
                it.isValidOrder(precedences)
            }.sumOf {
                it.middle().toLong()
            }
    }

    fun part2(input: String): Long {
        val sections = input.toSections()

        // First section is page precedence rules
        val precedences =
            sections[0]
                .lines()
                .map {
                    it.split("|")
                }.groupBy(
                    { it.first() },
                    { it.last() },
                )

        // Second section is page orders
        return sections[1]
            .lines()
            .map {
                it.split(",")
            }.filterNot {
                it.isValidOrder(precedences)
            }.map {
                it.reorder(precedences)
            }.sumOf {
                it.middle().toLong()
            }
    }

    /**
     * Determines if this list of page number is in a valid ordering per
     * the given page precedence rules.
     * @param precedences Page precedence rules.
     * @return True if valid, false otherwise.
     */
    private fun List<String>.isValidOrder(precedences: Map<String, List<String>>): Boolean {
        val processed = mutableListOf<String>()
        forEach { page ->
            // Ensure no page, if any, in the precedence has already been processed
            val precedence = precedences[page]
            if (precedence != null && precedence.any { processed.contains(it) }) return false

            processed.add(page)
        }

        return true
    }

    /**
     * Reorders this list of page numbers so that it is a valid ordering
     * per the given page precedence rules.
     * @param precedences Page precedence rules.
     * @return Corrected list.
     */
    private fun List<String>.reorder(precedences: Map<String, List<String>>) =
        sortedWith { first, second ->
            val precedence = precedences[first]
            if (precedence != null && precedence.contains(second)) 1 else -1
        }

    /**
     * Gets the middle element of this list.
     * @return Middle element.
     */
    private fun List<String>.middle() = get(size / 2)

    /**
     * Replaces line endings in this string with the current environment's default.
     * @return String.
     */
    private fun String.fixLineEndings() =
        replace(
            "\\n|\\r\\n".toRegex(),
            System.lineSeparator(),
        )

    /**
     * Splits this string into sections on blank lines.
     * @return Splits.
     */
    private fun String.toSections() =
        fixLineEndings().split(
            System.lineSeparator() + System.lineSeparator(),
        )
}
