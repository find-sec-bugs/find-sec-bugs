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
package com.h3xstream.findsecbugs.file;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PathTraversalDetectorTest extends BaseDetectorTest {

    @Test
    public void detectPathTraversal() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/pathtraversal/PathTraversal")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(17, 18, 19, 20, 22, 23, 35, 36, 37)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("PATH_TRAVERSAL_IN")
                            .inClass("PathTraversal").inMethod("main").atLine(line)
                            .build()
            );
        }

        for (Integer line : Arrays.asList(25, 26, 27, 28)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("PATH_TRAVERSAL_OUT")
                            .inClass("PathTraversal").inMethod("main").atLine(line)
                            .build()
            );
        }
        
        verify(reporter, times(9)).doReportBug(bugDefinition().bugType("PATH_TRAVERSAL_IN").build());
        verify(reporter, times(4)).doReportBug(bugDefinition().bugType("PATH_TRAVERSAL_OUT").build());
    }
}
