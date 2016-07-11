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
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Map;

import static org.mockito.Mockito.*;

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
        Map<String, int[]> methodFalsePositiveLines = new HashMap<String, int[]>();
        methodFalsePositiveLines.put("safePlayConfig", new int[]{63, 64, 65, 66, 67, /**/ 69, 70, 71, 72, 73});
        methodFalsePositiveLines.put("safeEnvProperty", new int[]{81, 82, 83, 84, 85, /**/ 87, 88, 89, 90, 91});
        methodFalsePositiveLines.put("safeUntainted", new int[]{105, 106, 107, 108, 109, /**/ 111, 112, 113, 114, 115});
        methodFalsePositiveLines.put("safeNoSensitiveData", new int[]{119, 120, 121, 122, 123, /**/ 125, 126, 127, 128, 129});

        for (Entry<String, int[]> entry : methodFalsePositiveLines.entrySet()) {
            // Lets check every line specified above
            for (int line : entry.getValue()) {
                verify(reporter,never()).doReportBug(
                        bugDefinition()
                        .bugType("SCALA_SENSITIVE_DATA_EXPOSURE")
                        .inClass("SensitiveDataExposureController").inMethod(entry.getKey()).atLine(line)
                        .build()
                    );
            }
        }
    }
}
