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
package com.h3xstream.findsecbugs.file;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Test that Spring Environment.getProperty() values are treated as SAFE
 * and do not trigger PATH_TRAVERSAL_IN false positives.
 * Issue #776: Mark Spring Environment.getProperty as SAFE
 */
public class SpringEnvironmentSafeTest extends BaseDetectorTest {

    @Test
    public void avoidFpWithEnvironmentGetProperty() throws Exception {
        String[] files = {
                getClassFilePath("testcode/pathtraversal/SpringEnvironmentSafe")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("PATH_TRAVERSAL_IN")
                        .inClass("SpringEnvironmentSafe")
                        .inMethod("safePropertyUsage")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("PATH_TRAVERSAL_IN")
                        .inClass("SpringEnvironmentSafe")
                        .inMethod("safePropertyWithDefault")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("PATH_TRAVERSAL_IN")
                        .inClass("SpringEnvironmentSafe")
                        .inMethod("safeRequiredProperty")
                        .build()
        );
    }
}
