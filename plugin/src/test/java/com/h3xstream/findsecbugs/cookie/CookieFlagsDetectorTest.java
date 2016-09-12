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
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import java.util.List;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class CookieFlagsDetectorTest extends BaseDetectorTest {

    @Test
    public void detectSecureFlagCookieBasic() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/cookie/InsecureCookieSamples")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (String method : Arrays.asList("unsafeCookie1", "unsafeCookie2", "unsafeCookie4", "unsafeCookie5")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("INSECURE_COOKIE")
                            .inClass("InsecureCookieSamples").inMethod(method)
                            .build()
            );
        }

        // Advanced checks when multiple cookies are set
        List<Integer> lines = Arrays.asList(new Integer[] { 73, 77, 81, 85 });
        for (int line : lines) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("INSECURE_COOKIE")
                            .inClass("InsecureCookieSamples").inMethod("multipleCookies").atLine(line)
                            .build()
            );
        }
    }

    @Test
    public void avoidSecureFlagBasicFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/cookie/InsecureCookieSamples")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (String method : Arrays.asList("safeCookie1", "safeCookie2")) {
            verify(reporter,never()).doReportBug(
                    bugDefinition()
                            .bugType("INSECURE_COOKIE")
                            .inClass("InsecureCookieSamples").inMethod(method)
                            .build()
            );
        }

        // Advanced checks when multiple cookies are set
        verify(reporter, times(4)).doReportBug(
                bugDefinition()
                        .bugType("INSECURE_COOKIE")
                        .inClass("InsecureCookieSamples").inMethod("multipleCookies")
                        .build()
        );
    }

    @Test
    public void detectHttpOnlyCookieBasic() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/cookie/HttpOnlyCookieSamples")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (String method : Arrays.asList("unsafeCookie1", "unsafeCookie2", "unsafeCookie4", "unsafeCookie5")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HTTPONLY_COOKIE")
                            .inClass("HttpOnlyCookieSamples").inMethod(method)
                            .build()
            );
        }

        // Advanced checks when multiple cookies are set
        List<Integer> lines = Arrays.asList(new Integer[] { 76, 80, 84, 88 });
        for (int line : lines) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HTTPONLY_COOKIE")
                            .inClass("HttpOnlyCookieSamples").inMethod("multipleCookies").atLine(line)
                            .build()
            );
        }
    }

    @Test
    public void avoidHttpOnlyBasicFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/cookie/HttpOnlyCookieSamples")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (String method : Arrays.asList("safeCookie1", "safeCookie2")) {
            verify(reporter,never()).doReportBug(
                    bugDefinition()
                            .bugType("HTTPONLY_COOKIE")
                            .inClass("HttpOnlyCookieSamples").inMethod(method)
                            .build()
            );
        }

        // Advanced checks when multiple cookies are set
        // This method should not contain more than unsafe calls
        verify(reporter, times(4)).doReportBug(
                bugDefinition()
                        .bugType("HTTPONLY_COOKIE")
                        .inClass("HttpOnlyCookieSamples").inMethod("multipleCookies")
                        .build()
        );
    }
}
