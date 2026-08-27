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

@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.lillicoder.advent-of-code-kotlin")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
}

includeBuild("build-logic")
include(":2023:day1")
include(":2023:day2")
include(":2023:day3")
include(":2023:day4")
include(":2023:day5")
include(":2023:day6")
include(":2023:day7")
include(":2023:day8")
include(":2023:day9")
include(":2023:day10")
include(":2023:day11")
include(":2023:day12")
include(":2023:day13")
include(":2023:day14")
include(":2023:day15")
include(":2023:day16")
include(":2024:day1")
include(":2024:day2")
include(":2024:day3")
include(":2024:day4")
include(":2024:day5")
include(":2024:day6")
include(":2024:day7")
include(":2024:day8")
include(":2024:day9")
include(":2024:day10")
