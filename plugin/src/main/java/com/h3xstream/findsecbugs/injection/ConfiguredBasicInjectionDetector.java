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
package com.h3xstream.findsecbugs.injection;

import edu.umd.cs.findbugs.BugReporter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Detector designed for extension to detect basic injection with methods
 * configured in resource files using specified format
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public abstract class ConfiguredBasicInjectionDetector extends BasicInjectionDetector {

    private static final String SINK_CONFIG_PATH = "injection-sinks/";
    
    public ConfiguredBasicInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }
    
    protected void loadConfiguredSinks(String filename, String bugType) {
        assert filename != null && bugType != null && !bugType.isEmpty();
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader()
                    .getResourceAsStream(SINK_CONFIG_PATH.concat(filename));
            loadSinks(stream, bugType);
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    throw new RuntimeException("cannot close stream", ex);
                }
            }
        }
    }
    
    protected void loadSinks(InputStream input, String bugType) throws IOException {
        assert input != null && bugType != null && !bugType.isEmpty();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        for (;;) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.isEmpty() || line.startsWith("-")) {
                continue;
            }
            loadSink(line, bugType);
        }
    }
    
    protected void loadSink(String line, String bugType) {
        assert line != null && bugType != null && !bugType.isEmpty();
        String[] split = line.split("\\:");
        if (split.length != 2) {
            throw new IllegalArgumentException("There must be exactly one ':' in " + line);
        }
        String[] stringArguments = split[1].split(",");
        int length = stringArguments.length;
        if (length == 0) {
            throw new IllegalArgumentException("no injectable parameters specified in " + line);
        }
        
        int[] intArguments = new int[length];
        for (int i = 0; i < length; i++) {
            try {
                intArguments[i] = Integer.parseInt(stringArguments[i]);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("cannot parse " + stringArguments[i], ex);
            }
        }
        addSink(split[0], intArguments, bugType);
    }
}
