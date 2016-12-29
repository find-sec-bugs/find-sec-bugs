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

import static org.mockito.Mockito.*;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

public class PermissiveCORSDetectorTest extends BaseDetectorTest {

    @Test
    public void detectPermissiveCORS() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/PermissiveCORS")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        //1rst variation resp.addHeader("Access-Control-Allow-Origin", "*")
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PERMISSIVE_CORS")
                        .inClass("PermissiveCORS").inMethod("addPermissiveCORS").atLine(25)
                        .build()
        );
        //2nd variation resp.setHeader("Access-Control-Allow-Origin", "*");
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PERMISSIVE_CORS")
                        .inClass("PermissiveCORS").inMethod("setPermissiveCORS").atLine(29)
                        .build()
        );

        verify(reporter, times(2)).doReportBug(
                bugDefinition()
                        .bugType("PERMISSIVE_CORS")
                        .build()
        );
    }
}
