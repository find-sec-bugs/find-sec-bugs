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

abstract class IdentityFunctionCommandInjection {

    fun `tained input executed after always true filter`(tainted: String) {
        val postFunctionTained = tainted.filter { true }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after always false not filter`(tainted: String) {
        val postFunctionTained = tainted.filterNot { false }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after identity function with run`(tainted: String) {
        val postFunctionTained = tainted.run { this }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after identity function with let`(tainted: String) {
        val postFunctionTained = tainted.let { it }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after identity function with apply`(tainted: String) {
        val postFunctionTained = tainted.apply { Runtime.getRuntime().exec(this) }
    }

    fun `tained input executed after drop while always false`(tainted: String) {
        val postFunctionTained = tainted.dropWhile { false }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after drop last while always false`(tainted: String) {
        val postFunctionTained = tainted.dropLastWhile { false }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after take while always true`(tainted: String) {
        val postFunctionTained = tainted.takeWhile { true }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after take last while always true then reversed`(tainted: String) {
        val postFunctionTained = tainted.takeLastWhile { true }.reversed()

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after trim always false`(tainted: String) {
        val postFunctionTained = tainted.trim { false }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after trim end always false`(tainted: String) {
        val postFunctionTained = tainted.trimEnd { false }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after trim start always false`(tainted: String) {
        val postFunctionTained = tainted.trimStart { false }

        Runtime.getRuntime().exec(postFunctionTained)
    }

    fun `tained input executed after also does nothing`(tainted: String) {
        val postFunctionTained = tainted.also { }

        Runtime.getRuntime().exec(postFunctionTained)
    }
}
