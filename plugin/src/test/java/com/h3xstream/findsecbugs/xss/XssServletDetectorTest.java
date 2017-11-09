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
import java.util.Arrays;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class XssServletDetectorTest extends BaseDetectorTest {

    @BeforeMethod
    public void beforeTest() {
        FindSecBugsGlobalConfig.getInstance().setReportPotentialXssWrongContext(true);
    }

    @AfterMethod
    public void afterTest() {
        FindSecBugsGlobalConfig.getInstance().setReportPotentialXssWrongContext(false);
    }

    @Test
    public void detectXssServlet1() throws Exception {
        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet1") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet1").inMethod("doGet")
                .withPriority("High").atLine(17).build());

        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet1").inMethod("doGet")
                .withPriority("Low").atLine(19).build());
        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet1").inMethod("doGet")
                .withPriority("Low").atLine(20).build());
        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet1").inMethod("doGet")
                .withPriority("High").atLine(22).build());
        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet1").inMethod("doGet")
                .withPriority("High").atLine(23).build());

        verify(reporter, times(5)).doReportBug(bugDefinition().bugType("XSS_SERVLET").build());

    }

    @Test
    public void detectXssServlet_separateMethodBasic() throws Exception {
        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet2") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet2")
                .inMethod("indirectWrite").withPriority("High").build());

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_SERVLET").build());
    }

    @Test
    public void detectXssServlet_safeEncoders() throws Exception {
        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet3") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(24, 28, 30)) {
            verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet3")
                    .inMethod("writeWithEncoders").withPriority("High").atLine(line).build());
        }
        for (Integer line : Arrays.asList(26, 27, 29, 31)) {
            verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet3")
                    .inMethod("writeWithEncoders").withPriority("Low").atLine(line).build());
        }
        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet3")
                .inMethod("uncalledSink").withPriority("Medium").build());
        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet3")
                .inMethod("sinkCalledOnlyWithEncoded").withPriority("Low").build());
        verify(reporter, times(3 + 4 + 2)).doReportBug(bugDefinition().bugType("XSS_SERVLET").build());
    }

    @Test
    public void detectXssServlet_basicTaintAnalysis() throws Exception {
        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet4") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet4")
                .inMethod("writeWithStringBuilder").withPriority("High").build());

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_SERVLET").build());
    }

    @Test
    public void detectXssServlet_variousMethodSignatures() throws Exception {
        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet5") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, times(4)).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testWrite").build());

        verify(reporter, times(6)).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testFormatUnsafe").build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testFormatSafe").build());

        verify(reporter, times(4 * 2)).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testPrintUnsafe").build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testPrintSafe").build());

        verify(reporter, times(6)).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testPrintfUnsafe").build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testPrintfSafe").build());

        verify(reporter, times(3)).doReportBug(
                bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet5").inMethod("testAppend").build());

        verify(reporter, times(27)).doReportBug(bugDefinition().bugType("XSS_SERVLET").build());
    }

    @Test
    public void detectXssServlet6() throws Exception {

        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet6") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet6")
                .inMethod("doPost").withPriority("Low").build());
    }

    @Test
    public void detectXssServlet6_hideByDefault() throws Exception {
        // This test make sure that the default configuration will hide value that are
        // escaped.
        FindSecBugsGlobalConfig.getInstance().setReportPotentialXssWrongContext(false);

        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet6") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet6")
                .inMethod("doPost").withPriority("Low").build());
    }

    @Test
    public void detectXssServlet7() throws Exception {
        // Locate test code
        String[] files = { getClassFilePath("testcode/xss/servlets/XssServlet7") };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet7")
                .inMethod("doPost").atLine(32).withPriority("High").build());

        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet7")
                .inMethod("doPost").atLine(34).withPriority("Low").build());

        verify(reporter).doReportBug(bugDefinition().bugType("XSS_SERVLET").inClass("XssServlet7")
                .inMethod("doPost").atLine(35).withPriority("Low").build());

        verify(reporter, times(3)).doReportBug(bugDefinition().bugType("XSS_SERVLET").build());
    }
}
