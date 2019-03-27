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
package com.h3xstream.findsecbugs.xss;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class XSSRequestWrapperDetectorTest extends BaseDetectorTest {

    @Test
    public void detectXssWrapper1() throws Exception {
        detectXssWrapperFromClass("XSSRequestWrapper");
    }

    @Test
    public void detectXssWrapper2() throws Exception {
        detectXssWrapperFromClass("XSSRequestWrapper2");
    }

    private void detectXssWrapperFromClass(String className) throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xss/" + className)
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_REQUEST_WRAPPER")
                        .inClass(className)
                        .build()
        );
    }

    @Test
    public void avoidFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xss/FalsePositiveRequestWrapper")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XSS_REQUEST_WRAPPER")
                        .build()
        );
    }

}
