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
import java.util.Arrays;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testng.annotations.Test;

public class HttpResponseSplittingDetectorTest extends BaseDetectorTest {
    
    @Test
    public void detectResponseSplitting() throws Exception {
        String[] files = {
                getClassFilePath("testcode/ResponseSplittingServlet")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(15, 18)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HTTP_RESPONSE_SPLITTING")
                            .inClass("ResponseSplittingServlet").inMethod("doGet").withPriority("Low").atLine(line)
                            .build()
            );
        }
        for (Integer line : Arrays.asList(16, 17, 21, 37)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HTTP_RESPONSE_SPLITTING")
                            .inClass("ResponseSplittingServlet").withPriority("Medium").atLine(line)
                            .build()
            );
        }
        for (Integer line : Arrays.asList(32, 33)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HTTP_RESPONSE_SPLITTING")
                            .inClass("ResponseSplittingServlet").inMethod("doGet").withPriority("Medium").atLine(line)
                            .build()
            );
        }
        verify(reporter, times(8)).doReportBug(bugDefinition().bugType("HTTP_RESPONSE_SPLITTING").build());
    }
}
