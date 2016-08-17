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
import java.util.Arrays;

import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

public class MethodSummaryValidationTest {
    private static final boolean DEBUG = true;

    TaintMethodSummaryMapLoader loader = new TaintMethodSummaryMapLoader();

    @Test
    public void validateMethodSummaries() throws IOException {
        for(String directory : Arrays.asList("/taint-config", "/safe-encoders")) {
            InputStream in = getClass().getResourceAsStream(directory);
            assertNotNull(in, "Unable list the resources in the taint-config directory");

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String file;
            while ((file = br.readLine()) != null) {
                if (DEBUG) {
                    System.out.println("File : " + file);
                }

                ////////////
                // Validate annotation (list of annotation with parameters)
                if ("taint-param-annotations.txt".equals(file)) {

                    BufferedReader br2 = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = br2.readLine()) != null) {
                        String annotation = line.replace('/', '.');
                        validateClass(annotation);
                    }

                }
                ////////////
                // Validate method summaries
                else {
                    InputStream inFile = getClass().getResourceAsStream(directory+"/"+ file);
                    assertNotNull(inFile, "File not found: "+ directory+ "/"+file);
                    validateFile(inFile);
                }
            }
        }

    }

    public void validateFile(InputStream inFile) throws IOException {
        loader.load(inFile, new TaintMethodSummaryMapLoader.TaintMethodSummaryReceiver() {
            @Override
            public void receiveTaintMethodSummary(String typeSignature, String summary) throws IOException {
                if (DEBUG) {
                    System.out.println("[?] fmn: " + typeSignature);
                }
                String[] methodParts = typeSignature.split("\\.");

                //Test the validity of the class name
                String className = methodParts[0].replace('/','.');
                validateClass(className);
            }
        });
    }

    public void validateClass(String className) {
        if (className.startsWith("scala")) {
            return;
        }
        if (className.startsWith("L") && className.endsWith(";")) {
            className = className.substring(1, className.length() - 1);
        }
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.err.println("[!] Class not found "+className); //FIXME: Replace with assert
        }
    }
}
