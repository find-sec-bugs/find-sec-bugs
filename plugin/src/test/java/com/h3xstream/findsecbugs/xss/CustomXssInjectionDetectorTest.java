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

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class CustomXssInjectionDetectorTest extends BaseDetectorTest {

    @BeforeTest
    public void before() {
        String path = this.getClass().getResource("/com/h3xstream/findsecbugs/injection/custom/CustomXssInjectionSource.txt").getPath();
        System.setProperty("findsecbugs.injection.customconfigfile.CustomXssInjectionDetector", path + "|CUSTOM_XSS_INJECTION");
    }

    @Test
    public void detectInjection() throws Exception {
        //Logger.setLevel(Level.DEBUG.levelInt);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xss/CustomXssInjection")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("CUSTOM_XSS_INJECTION")
                        .inClass("CustomXssInjection").inMethod("testInjection").atLine(12)
                        .build()
        );
        
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("CUSTOM_XSS_INJECTION")
                        .inClass("CustomXssInjection").inMethod("testInjection").atLine(14)
                        .build()
        );

    }
}
