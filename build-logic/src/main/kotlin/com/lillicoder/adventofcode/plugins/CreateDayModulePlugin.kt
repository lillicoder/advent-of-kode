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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

/**
 * [Plugin] that enables a task to create a day module for an Advent of Code puzzle.
 *
 * Configurable with [CreateDayModuleExtension].
 */
class CreateDayModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("createDayModule", CreateDayModuleExtension::class.java)
        target.tasks.register<CreateDayModuleTask>("createDayModule") {
            description = "Creates a new day module for a given year and day."
            group = "advent-of-code"
            day.set(extension.day)
            year.set(extension.year)
        }
    }
}
