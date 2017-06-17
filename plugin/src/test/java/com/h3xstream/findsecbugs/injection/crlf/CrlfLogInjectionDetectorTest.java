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
package com.h3xstream.findsecbugs.injection.crlf;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testng.annotations.Test;

public class CrlfLogInjectionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectResponseSplitting() throws Exception {
        String[] files = {
            getClassFilePath("testcode/Logging")
        };
        SecurityReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (int line = 20; line < 49; line++) {
            verify(reporter).doReportBug(
                    bugDefinition()
                    .bugType("CRLF_INJECTION_LOGS")
                    .inClass("Logging").inMethod("javaUtilLogging").atLine(line)
                    .build()
            );
        }
        verify(reporter, times(49 - 20)).doReportBug(bugDefinition().bugType("CRLF_INJECTION_LOGS").build());
    }
}
