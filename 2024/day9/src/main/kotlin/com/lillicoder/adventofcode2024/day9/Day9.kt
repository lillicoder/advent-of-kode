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

import com.lillicoder.adventofcode.kotlin.io.Resources
import java.util.Collections

fun main() {
    val day9 = Day9()
    val input = Resources.text("input.txt") ?: throw IllegalArgumentException("Could not read input.")
    println("The filesystem checksum after block-based compaction is ${day9.part1(input)}.")
    println("The filesystem checksum after file-based compaction is ${day9.part2(input)}.")
}

class Day9 {
    fun part1(input: String): Long {
        val expanded = input.expandBlocks()
        val compacted = expanded.compactByBlock()
        return compacted.checksum()
    }

    fun part2(input: String): Long {
        val expanded = input.expandBlocks()
        val compacted = expanded.compactByFile()
        return compacted.checksum()
    }

    /**
     * Halves this integer.
     * @return Half.
     */
    private fun Int.half() = this / 2

    /**
     * Determines if this integer is even.
     * @return True if even, false if odd.
     */
    private fun Int.isEven() = this % 2 == 0

    /**
     * [groupBy] that provide element index during grouping.
     * @param keySelector Key selector.
     * @param valueTransform Value transform.
     * @return Grouped elements.
     */
    private fun <T, K, V> Iterable<T>.groupByIndexed(
        keySelector: (Int, T) -> K,
        valueTransform: (Int, T) -> V,
    ): Map<K, List<V>> {
        val destination = LinkedHashMap<K, MutableList<V>>()
        forEachIndexed { index, element ->
            val key = keySelector(index, element)
            val list = destination.getOrPut(key) { ArrayList<V>() }
            list.add(valueTransform(index, element))
        }
        return destination
    }

    /**
     * Computes the checksum for this list of [Block].
     * @return Checksum of all blocks.
     */
    private fun List<Block>.checksum() =
        foldIndexed(0L) { index, accumulator, block ->
            accumulator +
                when (block.id) {
                    null -> 0L
                    else -> index * block.id
                }
        }

    /**
     * Compacts these [Block] such that all file blocks are contiguous regardless
     * of file integrity.
     * @return Compacted blocks.
     */
    private fun List<Block>.compactByBlock(): List<Block> {
        val queue =
            ArrayDeque(
                filterNot {
                    it.isEmpty()
                },
            )
        return map { block ->
            when (queue.isEmpty()) {
                // Exhausted all file blocks, pad with empty blocks
                true -> emptyBlock()
                // Last item in queue is file block to swap in,
                // first item in queue is existing file block that should be preserved
                else -> if (block.isEmpty()) queue.removeLast() else queue.removeFirst()
            }
        }
    }

    /**
     * Compacts these [Block] such that all files blocks have minimal empty blocks
     * between them. File integrity is preserved.
     * @return Compacted blocks.
     */
    private fun List<Block>.compactByFile(): List<Block> {
        val files = fileIndices().reversed()

        val copy: MutableList<Block> = toMutableList()
        files.forEach {
            val gap = copy.findGap(it)
            if (gap != null) copy.swap(gap, it)
        }

        return copy
    }

    /**
     * Gets all sub-lists of empty [Block] indices.
     * @return Empty block indices.
     */
    private fun List<Block>.emptyIndices(): List<List<Int>> {
        val gaps = mutableListOf<MutableList<Int>>()

        var current = mutableListOf<Int>()
        forEachIndexed { index, block ->
            if (block.isEmpty()) {
                current.add(index)
            } else if (current.isNotEmpty()) {
                gaps.add(current)
                current = mutableListOf()
            }
        }

        return gaps
    }

    /**
     * Finds a subset of these [Block] that are empty and can fit the given
     * file block indices.
     * @param file File block indices to find a gap for.
     * @return Gap or null if there is no gap that can accommodate the given file indices.
     */
    private fun List<Block>.findGap(file: List<Int>) =
        emptyIndices().firstOrNull {
            it.last() < file.first() && it.size >= file.size
        }

    private fun List<Block>.fileIndices() =
        groupByIndexed(
            { _, element ->
                element.id
            },
            { index, _ ->
                index
            },
        ).filterKeys {
            it != null
        }.map {
            it.value
        }

    /**
     * Swaps the given list of indices with each other in-order.
     * @param first First indices.
     * @param second Second indices.
     */
    private fun <T> MutableList<T>.swap(
        first: List<Int>,
        second: List<Int>,
    ) {
        first.zip(second).forEach {
            swap(
                it.first,
                it.second,
            )
        }
    }

    /**
     * Swaps the elements in this list at the given indices.
     * @param first First index.
     * @param second Second index.
     */
    private fun <T> MutableList<T>.swap(
        first: Int,
        second: Int,
    ) = Collections.swap(this, first, second)

    /**
     * Expands this string by repeating each character's file index N times, where
     * N is the integer value of that character and the file index is the
     * index of that character divided by 2.
     * @return Expanded string.
     */
    private fun String.expandBlocks() =
        flatMapIndexed { index, char ->
            val length = char.digitToInt()

            // Even indices are file blocks, odd indices are empty blocks
            val symbol =
                when (index.isEven()) {
                    true -> fileBlock(index.half().toLong())
                    else -> emptyBlock()
                }
            List(length) { symbol }
        }

    /**
     * Represents a single block.
     * @param symbol '.' for empty blocks, '#' for file blocks.
     * @param id File ID or null if this block is empty space.
     */
    private data class Block(
        val symbol: String,
        val id: Long? = null,
    ) {
        fun isEmpty() = symbol == "."
    }

    /**
     * Creates a new empty [Block].
     * @return Empty block.
     */
    private fun emptyBlock() = Block(".")

    /**
     * Creates a new file [Block].
     * @param id File ID.
     * @return File block.
     */
    private fun fileBlock(id: Long) = Block("#", id)
}
