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
package com.h3xstream.findsecbugs.cookie;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InsecureCookieDetectorTest extends BaseDetectorTest {

    @Test
    public void detectInsecureCookieBasic() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/cookie/InsecureCookieSamples")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (String method : Arrays.asList("unsafeCookie1", "unsafeCookie2", "unsafeCookie4", "unsafeCookie5")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("INSECURE_COOKIE")
                            .inClass("InsecureCookieSamples").inMethod(method)
                            .build()
            );
        }
    }

    @Test
    public void avoidBasicFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/cookie/InsecureCookieSamples")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (String method : Arrays.asList("safeCookie1", "safeCookie2")) {
            verify(reporter,never()).doReportBug(
                    bugDefinition()
                            .bugType("INSECURE_COOKIE")
                            .inClass("InsecureCookieSamples").inMethod(method)
                            .build()
            );
        }
    }
}
