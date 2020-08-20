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
package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ImproperHandlingUnicodeDetectorTest extends BaseDetectorTest {

    @Test
    public void detectImproperStringNormalization() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/unicode/RiskyNormalizationSample.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(32,33,34,35, 36, 39)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("IMPROPER_UNICODE")
                            .inClass("RiskyNormalizationSample")
                            .inMethod("stringNormalizationSuite")
                            .withPriority("Low")
                            .atLine(line)
                            .build()
            );
        }

        verify(reporter, times(6)).doReportBug(
                bugDefinition()
                        .bugType("IMPROPER_UNICODE")
                        .inClass("RiskyNormalizationSample")
                        .inMethod("stringNormalizationSuite").build()
        );
    }

    @Test
    public void detectImproperCaseMapping() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/unicode/RiskyCaseMappingSample.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(21,22,23)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("IMPROPER_UNICODE")
                            .inClass("RiskyCaseMappingSample")
                            .inMethod("caseMappingSuite")
                            .withPriority("Low")
                            .atLine(line)
                            .build()
            );
        }

        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("IMPROPER_UNICODE")
                        .inClass("RiskyCaseMappingSample")
                        .inMethod("caseMappingSuite").build()
        );
    }


    @Test
    public void detectImproperCaseMappingAvoidFP() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/unicode/RiskyCaseMappingSample.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("IMPROPER_UNICODE")
                        .inClass("RiskyCaseMappingSample")
                        .inMethod("caseMappingFalsePositive").build()
        );
    }
}
