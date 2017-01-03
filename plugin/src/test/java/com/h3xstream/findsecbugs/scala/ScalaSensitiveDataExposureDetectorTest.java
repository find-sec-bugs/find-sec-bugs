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
package com.h3xstream.findsecbugs.scala;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ScalaSensitiveDataExposureDetectorTest extends BaseDetectorTest {

    @Test
    public void detectSensitiveDataExposure() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(false);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_sensitive_data_exposure.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions for bugs
        Map<String, int[]> methodBugLines = new HashMap<String, int[]>();
        methodBugLines.put("vulnerable1", new int[]{16, 17, 18, 19, 20, /**/ 22, 23, 24, 25, 26});
        methodBugLines.put("vulnerable2", new int[]{33, 34, 35, 36, 37, /**/ 39, 40, 41, 42, 43});
        methodBugLines.put("vulnerable3", new int[]{49, 50, 51, 52, 53});
        methodBugLines.put("vulnerableCookie1", new int[]{63, 64, 65, 66});
        methodBugLines.put("vulnerableCookie2", new int[]{73, 74, 75, 76});
        methodBugLines.put("vulnerableCookie3", new int[]{82, 83});

        for (Entry<String, int[]> entry : methodBugLines.entrySet()) {
            // Lets check every line specified above
            for (int line : entry.getValue()) {
                verify(reporter).doReportBug(
                        bugDefinition()
                        .bugType("SCALA_SENSITIVE_DATA_EXPOSURE")
                        .inClass("SensitiveDataExposureController").inMethod(entry.getKey()).atLine(line)
                        .build()
                    );
            }
        }


        //Assertions for safe calls and false positives
        List<String> methodFalsePositives = new ArrayList<String>();
        methodFalsePositives.add("safePlayConfig");
        methodFalsePositives.add("safeEnvProperty");
        methodFalsePositives.add("safeUntainted");
        methodFalsePositives.add("safeNoSensitiveData");

        for (String method : methodFalsePositives) {
            // Lets check every method specified above
            verify(reporter,never()).doReportBug(
                    bugDefinition()
                            .bugType("SCALA_SENSITIVE_DATA_EXPOSURE")
                            .inClass("SensitiveDataExposureController").inMethod(method)
                            .build()
            );
        }
    }
}
