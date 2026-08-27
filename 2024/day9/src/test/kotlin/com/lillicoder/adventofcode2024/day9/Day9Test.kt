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

package com.lillicoder.adventofcode2024.day9

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day9].
 */
internal class Day9Test {
    private val input =
        """
        2333133121414131402
        """.trimIndent()
    private val day9 = Day9()

    @Test
    fun part1() {
        val expected = 1928L
        val actual = day9.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 2858L
        val actual = day9.part2(input)
        assertEquals(expected, actual)
    }
}
