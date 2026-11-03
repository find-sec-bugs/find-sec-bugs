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
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.*;

import static org.mockito.Mockito.*;

/**
 * Tests XSS detection in JSPs compiled against the Jakarta namespace (jakarta.servlet.jsp.*).
 *
 * Before running these tests, JSPs need to be compiled:
 * <pre>mvn -pl findsecbugs-samples-jsp-jakarta test-compile</pre>
 */
public class JspXssJakartaDetectorTest extends BaseDetectorTest {

    @BeforeMethod
    public void beforeTest() {
        FindSecBugsGlobalConfig.getInstance().setReportPotentialXssWrongContext(true);
    }

    @AfterMethod
    public void afterTest() {
        FindSecBugsGlobalConfig.getInstance().setReportPotentialXssWrongContext(false);
    }

    @Test
    public void detectXssDirectUse() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/xss/xss_1_direct_use.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("jakarta/xss/xss_1_direct_use.jsp")
                        .atJspLine(10)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("jakarta/xss/xss_1_direct_use.jsp")
                        .atJspLine(15)
                        .build()
        );

        verify(reporter, times(2)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssTransferLocal() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/xss/xss_2_transfer_local.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("jakarta/xss/xss_2_transfer_local.jsp")
                        .atJspLine(10)
                        .build()
        );

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssMultipleTransferLocal() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/xss/xss_5_multiple_transfer_local.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("jakarta/xss/xss_5_multiple_transfer_local.jsp")
                        .atJspLine(13)
                        .build()
        );

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssGetParameter() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/xss/xss_6_get_parameter.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("jakarta/xss/xss_6_get_parameter.jsp")
                        .atJspLine(7)
                        .build()
        );

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssFalsePositiveSafeInput() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/xss/xss_3_false_positive_static_function.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssFalsePositiveOverwriteLocal() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/xss/xss_4_false_positive_overwrite_local.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssFalsePositiveDirectCast() throws Exception {
        String[] files = {
                getJspFilePath("jakarta/xss/xss_7_false_positive_direct_cast.jsp")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }
}
