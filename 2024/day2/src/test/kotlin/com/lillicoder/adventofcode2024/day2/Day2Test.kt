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

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day2].
 */
internal class Day2Test {
    private val input =
        """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
        """.trimIndent()
    private val day2 = Day2()

    @Test
    fun part1() {
        val expected = 2L
        val actual = day2.part1(input)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 4L
        val actual = day2.part2(input)
        assertEquals(expected, actual)
    }
}
