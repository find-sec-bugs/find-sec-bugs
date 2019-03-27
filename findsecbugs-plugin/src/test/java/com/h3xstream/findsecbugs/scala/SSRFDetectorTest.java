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
import java.util.Map;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class SSRFDetectorTest extends BaseDetectorTest {

    private static final String SCALA_PLAY_SSRF_TYPE = "SCALA_PLAY_SSRF";
    private static final String URLCONNECTION_SSRF_FD = "URLCONNECTION_SSRF_FD";

    @Test
    public void detectSSRFInController() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(false);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_play_ssrf.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        //Assertions for bugs
        Map<String, int[]> methodBugLines = new HashMap<String, int[]>();
        methodBugLines.put("vulnerableGet", new int[]{27, 31, 32, 33, 34, 35, 36, 37, 38, 39, 47});
        methodBugLines.put("vulnerablePost", new int[]{58, 62, 67, 71, 72, 73, 74, 75, 76, 77, 78, 79, 86, 90, 95});

        for (Map.Entry<String, int[]> entry : methodBugLines.entrySet()) {
            // Lets check every line specified above
            for (int line : entry.getValue()) {
                verify(reporter).doReportBug(
                        bugDefinition()
                                .bugType(SCALA_PLAY_SSRF_TYPE)
                                .inClass("SSRFController").inMethod(entry.getKey()).atLine(line)
                                .build()
                );
            }
        }

        //Assertions for safe calls and false positives
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType(SCALA_PLAY_SSRF_TYPE)
                        .inClass("SSRFController").inMethod("safeGetNotTainted")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType(SCALA_PLAY_SSRF_TYPE)
                        .inClass("SSRFController").inMethod("safePostNotTainted")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType(SCALA_PLAY_SSRF_TYPE)
                        .inClass("SSRFController").inMethod("safeGetWithWhitelist")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType(SCALA_PLAY_SSRF_TYPE)
                        .inClass("SSRFController").inMethod("safePostWithWhitelist")
                        .build()
        );
    }

    @Test
    public void detectURLConnectionSSRF() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/UrlConnectionSSRF")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(7)).doReportBug(bugDefinition()
                .bugType(URLCONNECTION_SSRF_FD).inClass("UrlConnectionSSRF")
                .inMethod("testURL").build()
        );
        verify(reporter, times(1)).doReportBug(bugDefinition()
                .bugType(URLCONNECTION_SSRF_FD).inClass("UrlConnectionSSRF")
                .inMethod("testURI").build()
        );
    }
}
