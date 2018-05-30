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
package com.h3xstream.findsecbugs.graph;

import java.io.File;
import java.io.IOException;

public class TempDatabase {

    /**
     * Create an empty directory in temp directory.
     * The objective is to avoid polluting an obscure location or the code directory.
     *
     * https://stackoverflow.com/a/617438/89769
     *
     * @return
     * @throws IOException
     */
    public static File createTempDirectory()
            throws IOException
    {
        final File temp;

        temp = File.createTempFile("graph-fsb-", Long.toString(System.nanoTime()));

        if(!(temp.delete())) { throw new IOException("Could not delete temp file: " + temp.getAbsolutePath()); }
        if(!(temp.mkdir())) { throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());}

        return (temp);
    }
}
