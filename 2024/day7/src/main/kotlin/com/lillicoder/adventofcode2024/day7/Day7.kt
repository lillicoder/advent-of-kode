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

package com.lillicoder.adventofcode2024.day7

import com.lillicoder.adventofcode.kotlin.io.Resources
import com.lillicoder.adventofcode.kotlin.text.splitNotEmpty
import kotlin.math.pow

fun main() {
    val day7 = Day7()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The sum of all test values for (+, *) that can be satisfied is ${day7.part1(input)}.")
    println("The sum of all test values for (+, *, ||) that can be satisfied is ${day7.part2(input)}.")
}

class Day7 {
    fun part1(input: String): Long {
        val equations = input.lines().toEquations()
        return equations
            .filterSolvable(
                listOf(
                    "+",
                    "*",
                ),
            ) {
                it
                    .replace(
                        '0',
                        '+',
                    ).replace(
                        '1',
                        '*',
                    )
            }.sumOf {
                it.value
            }
    }

    fun part2(input: String): Long {
        // For the sake of not having to rework the character parsing,
        // I'm treating || as the same as |
        val equations = input.lines().toEquations()
        return equations
            .filterSolvable(
                listOf(
                    "+",
                    "*",
                    "|",
                ),
            ) {
                it
                    .replace(
                        '0',
                        '+',
                    ).replace(
                        '1',
                        '*',
                    ).replace(
                        '2',
                        '|',
                    )
            }.sumOf {
                it.value
            }
    }

    /**
     * Raises this integer to the given exponent.
     * @param exponent Exponent.
     * @return Power.
     */
    private fun Int.pow(exponent: Int) = toDouble().pow(exponent).toInt()

    /**
     * Filters this list of [Equation] to those solvable by the given
     * set of operations.
     * @param operations Operations.
     * @param transform Optional transform to apply to each permutation of operations.
     */
    private fun List<Equation>.filterSolvable(
        operations: List<String>,
        transform: ((String) -> String)? = null,
    ) = filter { equation ->
        val (result, operands) = equation
        operations
            .permutations(
                operands.size - 1,
                transform,
            ).any {
                result.isSolvedBy(
                    operands,
                    it.splitNotEmpty(""),
                )
            }
    }

    /**
     * Gets the possible permutations of values in this list of the given length.
     * @param length Permutation length.
     * @param transform Optional transform to apply to each permutation.
     * @return Permutations.
     */
    private fun List<String>.permutations(
        length: Int,
        transform: ((String) -> String)? = null,
    ): List<String> =
        IntRange(0, size.pow(length) - 1).map {
            val binary = it.toString(size).padStart(length, '0')
            transform?.invoke(binary) ?: binary
        }

    /**
     * Converts this list of strings to an equivalent list of [Equation].
     * @return Equations.
     */
    private fun List<String>.toEquations() =
        map { line ->
            val sections = line.splitNotEmpty(":")
            Equation(
                sections[0].toLong(),
                sections[1]
                    .splitNotEmpty(
                        " ",
                    ).map {
                        it.toLong()
                    },
            )
        }

    /**
     * Determines if this value can be produced by applying the given
     * operations to each pair of operands in order.
     * @param operands Operands.
     * @param operations Operations.
     * @return True if solved by given operands and operations, false otherwise.
     */
    private fun Long.isSolvedBy(
        operands: List<Long>,
        operations: List<String>,
    ) = operands
        .zip(
            // First op will always be adding our next value to 0
            listOf("+") + operations,
        ).fold(0L) { accumulator, pair ->
            pair.second.calculate(accumulator, pair.first)
        } == this

    private fun String.calculate(
        first: Long,
        second: Long,
    ) = when (this) {
        "*" -> first * second
        "+" -> first + second
        "|" -> "$first$second".toLong()
        else -> throw IllegalArgumentException("Bad operation.")
    }

    /**
     * Represents the test value and operands for an equation.
     * @param value Test value (a.k.a. solution).
     * @param operands Operands.
     */
    data class Equation(
        val value: Long,
        val operands: List<Long>,
    )
}
