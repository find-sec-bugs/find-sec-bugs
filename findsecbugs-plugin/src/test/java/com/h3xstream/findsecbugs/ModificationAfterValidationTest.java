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

public class ModificationAfterValidationTest extends BaseDetectorTest {

    @Test
    public void detectModificationAfterValidation() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/modify_validate/ModifyAfter.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("MODIFICATION_AFTER_VALIDATION")
                        .inClass("ModifyAfter")
                        .inMethod("validate")
                        .withPriority("Low")
                        .atLine(19)
                        .build()
            );
        
    }

    @Test
    public void detectModificationBeforeValidationAvoidFP() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/modify_validate/ModifyBefore.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("MODIFY_BEFORE_VALIDATION")
                        .inClass("ModifyBefore")
                        .inMethod("validate")
                        .withPriority("Low")
                        .atLine(13)
                        .build()
            );
        
    }
}
