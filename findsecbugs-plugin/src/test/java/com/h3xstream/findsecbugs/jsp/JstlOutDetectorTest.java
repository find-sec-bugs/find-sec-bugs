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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JstlOutDetectorTest extends BaseDetectorTest {

    @Test
    public void jspEscape1_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_escape_1.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,never()).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }

    @Test
    public void jspEscape2_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_escape_2.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,never()).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }

    @Test
    public void jspEscape3_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_escape_3.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_JSTL_OUT")
                        .inJspFile("jstl/jstl_escape_3.jsp")
                        .atJspLine(3)
                        .withPriority("Medium")
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }

    @Test
    public void jspEscape4_unknown() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_escape_4.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_JSTL_OUT")
                        .inJspFile("jstl/jstl_escape_4.jsp")
                        .atJspLine(3)
                        .withPriority("Low")
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }
}
