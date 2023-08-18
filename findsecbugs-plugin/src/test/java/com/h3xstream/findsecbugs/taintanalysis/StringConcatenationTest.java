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

import static org.testng.Assert.assertTrue;

import static org.mockito.Mockito.*;

/**
 * Tests Java 9+ string concatenation that uses invokedynamic.
 */
public class StringConcatenationTest extends BaseDetectorTest {

    @Test
    public void validateStringConcatenation() throws Exception {

        // Enable the Java 9+ workaround
        FindSecBugsGlobalConfig.getInstance().setWorkaroundVisitInvokeDynamic(true);

        //Locate test code
        String[] files = { getClassFilePath("testcode/taint/StringConcatenation") };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());

        analyze(files, reporter);

        // SQL

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_JDBC").build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_JDBC")
                        .inClass("StringConcatenation").inMethod("unsafeStringSqlConcatenation")
                        .withPriority("Medium")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_JDBC")
                        .inClass("StringConcatenation").inMethod("safeBooleanSqlConcatenation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_JDBC")
                        .inClass("StringConcatenation").inMethod("safeStringSqlConcatenation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_JDBC")
                        .inClass("StringConcatenation").inMethod("safeIntSqlConcatenation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_JDBC")
                        .inClass("StringConcatenation").inMethod("safeIntLongSqlConcatenation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_JDBC")
                        .inClass("StringConcatenation").inMethod("safeIntDoubleSqlConcatenation")
                        .build());

        // File

        verify(reporter, times(2)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN").build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("StringConcatenation").inMethod("unsafeCharFileConcatenation")
                        .withPriority("Medium")
                        .build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("StringConcatenation").inMethod("unsafeStringFileConcatenation")
                        .withPriority("Medium")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("StringConcatenation").inMethod("safeStringFileConcatenation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("StringConcatenation").inMethod("safeIntFileConcatenation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("StringConcatenation").inMethod("safeStringLongFileConcatenation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("PATH_TRAVERSAL_IN")
                        .inClass("StringConcatenation").inMethod("safeStringDoubleFileConcatenation")
                        .build());
    }
}
