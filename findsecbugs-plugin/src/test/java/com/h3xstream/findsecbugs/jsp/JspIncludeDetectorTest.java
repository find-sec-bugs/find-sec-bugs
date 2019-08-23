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

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class JspIncludeDetectorTest extends BaseDetectorTest {

    @Test
    public void jspInclude1_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("includes/jsp_include_1.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_INCLUDE")
                        .inJspFile("includes/jsp_include_1.jsp")
                        .atJspLine(4)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }

    @Test
    public void jspInclude2_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("includes/jsp_include_2_safe.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, never()).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }


    @Test
    public void jspInclude3_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("includes/jsp_include_3.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_INCLUDE")
                        .inJspFile("includes/jsp_include_3.jsp")
                        .atJspLine(8)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }



    @Test
    public void jspInclude4_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("includes/jsp_include_4_safe.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("JSP_INCLUDE")
                        .build()
        );
    }
}
