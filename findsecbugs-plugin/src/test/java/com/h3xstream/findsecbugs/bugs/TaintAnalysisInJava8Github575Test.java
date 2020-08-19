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
package com.h3xstream.findsecbugs.bugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * Test case to reproduce #575
 * https://github.com/find-sec-bugs/find-sec-bugs/issues/332#issuecomment-674015645
 */
public class TaintAnalysisInJava8Github575Test extends BaseDetectorTest {

    @Test
    public void testForRegression575() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/bugs/BenchmarkTest00051.java"),
                getClassFilePath("testcode/bugs/SeparateClassRequest.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION") //
                        .build()
        );
    }
}
