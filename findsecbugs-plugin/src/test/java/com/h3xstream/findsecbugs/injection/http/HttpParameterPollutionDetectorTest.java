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
package com.h3xstream.findsecbugs.injection.http;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class HttpParameterPollutionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectHttpParameterPollution() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/HttpParameterPollution")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(21, 24, 25)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HTTP_PARAMETER_POLLUTION")
                            .inClass("HttpParameterPollution").inMethod("doGet").atLine(line)
                            .build()
            );
        }

        //Out of 5 calls, 3 are suspicious
        verify(reporter, times(3)).doReportBug(
                bugDefinition().bugType("HTTP_PARAMETER_POLLUTION").build()
        );
    }


    @Test
    public void avoidHttpParameterPollutionFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/HttpParameterPollutionFalsePositive.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("HTTP_PARAMETER_POLLUTION")
                        .build()
        );

    }

}
