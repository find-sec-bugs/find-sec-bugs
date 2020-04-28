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

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;
import testcode.taint.StaticContextTaintPropagation;

import static org.mockito.Mockito.*;

public class StaticContextTaintPropagationTest extends BaseDetectorTest {

    @Test
    public void testBackPropagation() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/taint/StaticContextTaintPropagation")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        verify(reporter, never()).doReportBug(
            bugDefinition().bugType("SQL_INJECTION_HIBERNATE")
                .inClass("StaticContextTaintPropagation").inMethod("falsePositive")
                .build());

        verify(reporter, times(1)).doReportBug(
            bugDefinition().bugType("SQL_INJECTION_HIBERNATE")
                .inClass("StaticContextTaintPropagation").inMethod("taintedValue")
                .withPriority("High") // tainted value
                .build());

        verify(reporter, times(1)).doReportBug(
            bugDefinition().bugType("SQL_INJECTION_HIBERNATE")
                .inClass("StaticContextTaintPropagation").inMethod("staticContextTaintPropagationAfter")
                .withPriority("High") // tainted value
                .build());
    }

    @Test
    public void testIssue541() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/taint/StaticContextTaintPropagation$Issue541")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

    }
}