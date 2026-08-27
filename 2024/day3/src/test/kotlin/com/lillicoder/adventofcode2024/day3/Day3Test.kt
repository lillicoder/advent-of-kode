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

package com.lillicoder.adventofcode2024.day3

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Unit tests for [Day3].
 */
internal class Day3Test {
    private val input1 =
        """
        xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
        """.trimIndent()
    private val input2 =
        """
        xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
        """.trimIndent()
    private val day3 = Day3()

    @Test
    fun part1() {
        val expected = 161L
        val actual = day3.part1(input1)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expected = 48L
        val actual = day3.part2(input2)
        assertEquals(expected, actual)
    }
}
