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
import java.util.Arrays;
import java.util.List;
import org.testng.annotations.Test;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ScalaPathTraversalDetectorTest extends BaseDetectorTest {

    /**
     * Test the API coverage for PathTraversalDetector (loadConfiguredSinks("scala-path-traversal-in.txt", "SCALA_PATH_TRAVERSAL_IN"); and
     *                                                  loadConfiguredSinks("scala-path-traversal-out.txt", "PATH_TRAVERSAL_OUT");)
     * @throws Exception
     */
    @Test
    public void detectCommandInjection() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(false);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_path_traversal.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions for bugs
        List<Integer> linesVulnerableIn = Arrays.asList(19, 20, 21);
        for(int line : linesVulnerableIn) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SCALA_PATH_TRAVERSAL_IN")
                            .inClass("PathTraversalController").inMethod("vulnerableIn").atLine(line)
                            .build()
            );
        }

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PATH_TRAVERSAL_OUT")
                        .inClass("PathTraversalController").inMethod("vulnerableOut").atLine(28)
                        .build()
        );

        // Assertions for false positives
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("SCALA_PATH_TRAVERSAL_IN")
                        .inClass("PathTraversalController").inMethod("safeIn")
                        .build()
        );

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("PATH_TRAVERSAL_OUT")
                        .inClass("PathTraversalController").inMethod("safeOut")
                        .build()
        );
    }
}
