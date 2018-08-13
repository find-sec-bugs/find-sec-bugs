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
package com.h3xstream.findsecbugs.pathtraversal

import java.io.File
import java.io.IOException
import java.net.URISyntaxException

class PathTraversalKotlin {

    @Throws(IOException::class, URISyntaxException::class)
    fun main(args: Array<String>) {
        val filepath = args[1]

        // Unsafe
        createTempDir(filepath, filepath)
        createTempDir(filepath, filepath, File("static"))

        createTempFile(filepath, filepath)
        createTempFile(filepath, filepath, File("static"))

        // Safe
        createTempDir()
        createTempFile()
        createTempDir("static", "static")
        createTempFile("static", "static")
    }
}