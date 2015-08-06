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
package com.h3xstream.findsecbugs.taintanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Map of taint summaries for all known methods
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintMethodSummaryMap extends HashMap<String, TaintMethodSummary> {
    private static final long serialVersionUID = 1L;
    
    public void load(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                putFromLine(line);
            }
    }
    
    public void dump(PrintStream output) {
        TreeSet<String> keys = new TreeSet<String>(keySet());
        for (String key : keys) {
            output.println(key + ":" + get(key));
        }
    }
    
    private void putFromLine(String line) throws IOException {
        String[] tuple = line.split("\\:");
        if (tuple.length != 2) {
            throw new IOException("Line format is not 'method name:summary info'");
        }
        put(tuple[0].trim(), TaintMethodSummary.load(tuple[1]));
    }
}
