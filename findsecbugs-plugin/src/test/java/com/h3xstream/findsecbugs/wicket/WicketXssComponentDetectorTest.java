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
package com.h3xstream.findsecbugs.wicket;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WicketXssComponentDetectorTest extends BaseDetectorTest {

    @Test
    public void detectWicketXss() throws Exception {
        String[] files = {
                getClassFilePath("testcode/wicket/XssWicketExamplePage")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WICKET_XSS1")
                        .inClass("XssWicketExamplePage")
                        .withPriority("Medium")
                        .build()
        );
        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("WICKET_XSS1").build());
    }


    @Test
    public void detectWicketXssUnknown() throws Exception {
        String[] files = {
                getClassFilePath("testcode/wicket/XssUnknownWicketExamplePage")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WICKET_XSS1")
                        .inClass("XssUnknownWicketExamplePage")
                        .withPriority("Low")
                        .build()
        );
        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("WICKET_XSS1").build());
    }

    @Test
    public void avoidWicketXssFalsePositive() throws Exception {
        String[] files = {
                getClassFilePath("testcode/wicket/XssSafeWicketExamplePage")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, times(0)).doReportBug(bugDefinition().bugType("WICKET_XSS1").build());
    }
}
