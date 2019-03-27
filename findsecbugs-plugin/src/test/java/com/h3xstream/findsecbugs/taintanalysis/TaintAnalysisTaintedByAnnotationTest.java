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
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * These test cases cover the creation of taintness based on annotation such as @QueryParam and @PathParam (Spring)
 * @see com.h3xstream.findsecbugs.taintanalysis.TaintAnalysis#initEntryFact(com.h3xstream.findsecbugs.taintanalysis.TaintFrame)
 */
public class TaintAnalysisTaintedByAnnotationTest extends BaseDetectorTest {
    @Test
    public void testSafeTestCases() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/taint/SafeTaintedByAnnotationEndpoint")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("High").build());

        verify(reporter, times(10)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("Medium").build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("Low").build());


        verify(reporter,times(10)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").build());
    }

    @Test
    public void testUnsafeTestCases() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/taint/UnsafeTaintedByAnnotationEndpoint")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter, times(10)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("High").build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("Medium").build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("Low").build());


        verify(reporter,times(10)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").build());
    }

    @Test
    public void testVariousTaintedAnnotations() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/taint/VariousTaintedAnnotation")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").inMethod("requestParam").withPriority("High").build());

        verify(reporter).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").inMethod("pathVariable").withPriority("High").build());

        verify(reporter).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").inMethod("requestBody").withPriority("High").build());

        verify(reporter).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").inMethod("requestHeader").withPriority("High").build());

        verify(reporter,times(4)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("High").build());
    }
}
