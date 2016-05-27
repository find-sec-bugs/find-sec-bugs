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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testng.annotations.Test;

public class ExternalConfigurationControlDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCatalog() throws Exception {
        String[] files = {
            getClassFilePath("testcode/DbCatalog")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                .bugType("EXTERNAL_CONFIG_CONTROL")
                .inClass("DbCatalog").atLine(11)
                .build()
        );
        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("EXTERNAL_CONFIG_CONTROL").build());
    }
}
