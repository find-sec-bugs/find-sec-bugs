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

public class NormalizationAfterValidationTest extends BaseDetectorTest {

    @Test
    public void detectNormalizationAfterValidation() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/normalize/NormalizeAfter.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("NORMALIZATION_AFTER_VALIDATION")
                        .inClass("NormalizeAfter")
                        .inMethod("validate")
                        .withPriority("Low")
                        .atLine(19)
                        .build()
            );
        
    }

    @Test
    public void detectNormalizationBeforeValidationAvoidFP() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/normalize/NormalizeBefore.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("NORMALIZATION_AFTER_VALIDATION")
                        .inClass("NormalizeBefore")
                        .inMethod("validate")
                        .withPriority("Low")
                        .atLine(7)
                        .build()
            );
        
    }
}
