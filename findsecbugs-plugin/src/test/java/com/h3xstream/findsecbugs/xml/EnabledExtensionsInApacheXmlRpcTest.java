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
package com.h3xstream.findsecbugs.xml;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.util.Arrays;

public class EnabledExtensionsInApacheXmlRpcTest extends BaseDetectorTest {

    @Test
    public void detectEnabledExtensions() throws Exception {
        String[] files = {
                getClassFilePath("testcode/ApacheXmlRpc")
        };

        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(14, 15, 17, 18, 29, 31)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("RPC_ENABLED_EXTENSIONS")
                            .inClass("ApacheXmlRpc")
                            .inMethod("createClientAndServerConfigs")
                            .atLine(line)
                            .build()
            );
        };

        verify(reporter, times(6)).doReportBug(
                bugDefinition()
                        .bugType("RPC_ENABLED_EXTENSIONS")
                        .build()
        );

    }
}
