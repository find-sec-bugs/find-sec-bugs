/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.command

abstract class StringManipulationCommandInjection {

    fun `tained input executed after applying suffix with plus`(tainted: String) {
        val modifiedTained = tainted.plus("suffix")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after (de)capitalisation`(tainted: String) {
        val modifiedTained = tainted.capitalize().decapitalize()

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after first characters removed`(tainted: String) {
        val modifiedTained = tainted.drop(0)

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after last characters removed`(tainted: String) {
        val modifiedTained = tainted.dropLast(0)

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after front padding`(tainted: String) {
        val modifiedTained = tainted.padStart(10, ' ')

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after end padding`(tainted: String) {
        val modifiedTained = tainted.padEnd(10, ' ')

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after indentation`(tainted: String) {
        val modifiedTained = tainted.prependIndent("    ")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after remove prefix`(tainted: String) {
        val modifiedTained = tainted.removePrefix("prefix")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after remove range`(tainted: String) {
        val modifiedTained = tainted.removeRange(0, 0)

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after remove suffix`(tainted: String) {
        val modifiedTained = tainted.removeSuffix("suffix")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after remove surrounding`(tainted: String) {
        val modifiedTained = tainted.removeSurrounding("start", "end")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after replacement`(tainted: String) {
        val modifiedTained = tainted.replace("a", "a")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after replace first`(tainted: String) {
        val modifiedTained = tainted.replaceFirst("a", "a")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after replace after`(tainted: String) {
        val modifiedTained = tainted.replaceAfter("a", "a", "a")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after replace after last`(tainted: String) {
        val modifiedTained = tainted.replaceAfterLast("a", "a", "a")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after replace before`(tainted: String) {
        val modifiedTained = tainted.replaceBefore("a", "a", "a")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after replace before last`(tainted: String) {
        val modifiedTained = tainted.replaceBefore("a", "a", "a")

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after reversal`(tainted: String) {
        val modifiedTained = tainted.reversed()

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after trimming`(tainted: String) {
        val modifiedTained = tainted.trim()

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after trimming indent`(tainted: String) {
        val modifiedTained = tainted.trimIndent()

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after trimming start`(tainted: String) {
        val modifiedTained = tainted.trimStart()

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after trimming end`(tainted: String) {
        val modifiedTained = tainted.trimEnd()

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after front truncation`(tainted: String) {
        val modifiedTained = tainted.take(10)

        Runtime.getRuntime().exec(modifiedTained)
    }

    fun `tained input executed after end truncation`(tainted: String) {
        val modifiedTained = tainted.takeLast(10)

        Runtime.getRuntime().exec(modifiedTained)
    }
}
