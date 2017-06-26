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
package com.h3xstream.findsecbugs.spring;


import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SpringUnvalidatedRedirectDetectorTest extends BaseDetectorTest {

    @Test
    public void detectSpringUnvalidatedRedirect() throws Exception {
        //Locate test code
        String[] files = {
          getClassFilePath("testcode/spring/SpringUnvalidatedRedirectController")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("redirect1")
                        .atLine(13)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("redirect2")
                        .atLine(18)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("buildRedirect")
                        .atLine(28)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("redirect4")
                        .atLine(33)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("redirect5")
                        .atLine(38)
                        .build()
        );
    }
}
