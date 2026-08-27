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

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day10].
 */
internal class Day10Test {
    private val input =
        """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
        """.trimIndent()
    private val day10 = Day10()

    @Test
    fun part1() {
        val expected = 36L
        val actual = day10.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 81L
        val actual = day10.part2(input)
        assertEquals(expected, actual)
    }
}
