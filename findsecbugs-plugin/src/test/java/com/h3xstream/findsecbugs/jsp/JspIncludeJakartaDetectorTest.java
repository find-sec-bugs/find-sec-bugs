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
package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * Tests JSP include injection detection in JSPs compiled against the Jakarta namespace
 * (jakarta.servlet.*), exercising the jakarta signature branch in JspIncludeDetector.
 *
 * Before running these tests, JSPs need to be compiled:
 * <pre>mvn -pl findsecbugs-samples-jsp-jakarta test-compile</pre>
 */
public class JspIncludeJakartaDetectorTest extends BaseDetectorTest {

    @Test
    public void jspInclude1_unsafe() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/includes/jsp_include_1.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_INCLUDE")
                        .inJspFile("jakarta/includes/jsp_include_1.jsp")
                        .atJspLine(4)
                        .build()
        );

        verify(reporter).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }

    @Test
    public void jspInclude2_safe() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/includes/jsp_include_2_safe.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }

    @Test
    public void jspInclude4_safe() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/includes/jsp_include_4_safe.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }
}
