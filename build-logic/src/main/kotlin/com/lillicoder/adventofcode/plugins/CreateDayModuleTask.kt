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

package com.lillicoder.adventofcode.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.appendText
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.writeText

/**
 * Gradle task that creates a new day module for an Advent of Code puzzle.
 */
internal abstract class CreateDayModuleTask : DefaultTask() {
    /**
     * Day to make a module for (e.g. 10).
     */
    @get:Input
    abstract val day: Property<String>

    /**
     * Advent of code year (e.g. 2025).
     */
    @get:Input
    abstract val year: Property<String>

    /**
     * Creates a new day module for an Advent of Code puzzle. If the specified day already exists,
     * creation will be skipped.
     */
    @OptIn(ExperimentalPathApi::class)
    @TaskAction
    fun createModule() {
        val resolvedDay = day.get()
        val resolvedYear = year.get()

        if (resolvedDay.isEmpty() || resolvedYear.isEmpty()) {
            project.logger.lifecycle("Invalid day or year given, aborting. [day=$resolvedDay, year=$resolvedYear]")
        }

        val module = project.projectDir.toPath().resolve("$resolvedYear/day$resolvedDay")
        if (module.exists()) {
            project.logger.lifecycle("Module already exists, aborting. [path=$module]")
            return
        }

        try {
            module.createDirectories()
            module.createBuildScript()
            module.createSource(
                year = resolvedYear,
                day = resolvedDay,
            )
            module.createTestSource(
                year = resolvedYear,
                day = resolvedDay,
            )
            module.createInputResource()

            // Update settings.gradle.kts with new module
            val settingsScript =
                project.projectDir.toPath().resolve(
                    "settings.gradle.kts",
                )
            if (!settingsScript.exists()) {
                throw IllegalStateException(
                    "Could not find settings.gradle.kts, modules list not updated.",
                )
            }
            settingsScript.appendText(
                """
                include(":$resolvedYear/day$resolvedDay")
                
                """.trimIndent(),
            )

            project.logger.lifecycle("Module creation complete.")
        } catch (e: Exception) {
            project.logger.error("Module creation failed.", e)

            try {
                module.deleteRecursively()
            } catch (e: Exception) {
                project.logger.error("Failed to clean orphaned module.", e)
            }
        }
    }

    /**
     * Creates a build.gradle.kts child for this [Path].
     */
    private fun Path.createBuildScript() =
        resolve(
            "build.gradle.kts",
        ).writeText(
            """                    
            dependencies {
                implementation(libs.kotlin.stdlib)
                testImplementation(libs.kotlin.test)
            }
            
            tasks.test {
                useJUnitPlatform()
            }
            
            """.trimIndent(),
        )

    /**
     * Creates an input.txt resource file for this [Path].
     */
    private fun Path.createInputResource() =
        resolve(
            "src/main/resources/input.txt",
        ).createParentDirectories()
            .createFile()
            .writeText(
                """
                OMITTED
                
                """.trimIndent(),
            )

    /**
     * Creates a source file for this [Path] with a name matching the given year and day.
     * @param year Year (e.g. 2026).
     * @param day Day (e.g. 10).
     */
    private fun Path.createSource(
        year: String,
        day: String,
    ) = resolve(
        "src/main/kotlin/com/lillicoder/adventofcode$year/day$day/Day$day.kt",
    ).createParentDirectories()
        .createFile()
        .writeText(
            """
            package com.lillicoder.adventofcode$year.day$day

            fun main() {
                val day$day = Day$day()
            }
            
            class Day$day {
            }
            
            """.trimIndent(),
        )

    /**
     * Creates a test source file for this [Path] with a name matching the given year and day.
     * @param year Year (e.g. 2026).
     * @param day Day (e.g. 10).
     */
    private fun Path.createTestSource(
        year: String,
        day: String,
    ) = resolve(
        "src/test/kotlin/com/lillicoder/adventofcode$year/day$day/Day${day}Test.kt",
    ).createParentDirectories()
        .createFile()
        .writeText(
            """
            package com.lillicoder.adventofcode$year.day$day

            import kotlin.test.Test
            
            /**
             * Unit tests for [Day$day].
             */
            internal class Day${day}Test {
                private val input = ""
                private val day$day = Day$day()
                
                @Test
                fun part1() {
                }
                
                @Test
                fun part2() {
                }
            }
            
            """.trimIndent(),
        )
}
