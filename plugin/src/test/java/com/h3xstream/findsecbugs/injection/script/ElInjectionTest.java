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
package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ElInjectionTest extends BaseDetectorTest {
    @Test
    public void detectInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/script/ElExpressionSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("EL_INJECTION")
                        .inClass("ElExpressionSample")
                        .inMethod("unsafeEL")
                        .build()
        );

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("EL_INJECTION")
                        .inClass("ElExpressionSample")
                        .inMethod("safeEL")
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("EL_INJECTION")
                        .inClass("ElExpressionSample")
                        .inMethod("unsafeELMethod")
                        .build()
        );


        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("EL_INJECTION")
                        .inClass("ElExpressionSample")
                        .inMethod("safeELMethod")
                        .build()
        );

        //Only 2 instance in the sample class
        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("EL_INJECTION")
                        .inClass("ElExpressionSample")
                        .build()
        );
    }
}
