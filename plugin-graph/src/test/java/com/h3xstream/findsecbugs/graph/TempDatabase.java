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
