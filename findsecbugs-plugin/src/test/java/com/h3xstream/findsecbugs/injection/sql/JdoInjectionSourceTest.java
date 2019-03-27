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
package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JdoInjectionSourceTest extends BaseDetectorTest {

    @Test
    public void detectJdoInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/JdoSql")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(22, 24)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_JDO")
                            .inClass("JdoSql").inMethod("testJdoQueries").atLine(line)
                            .build()
            );
        }

        for (Integer line : Arrays.asList(39, 43, 47)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_JDO")
                            .inClass("JdoSql").inMethod("testJdoQueriesAdditionalMethodSig").atLine(line)
                            .build()
            );
        }

        //Only the previous 5 cases should be marked as vulnerable
        verify(reporter, times(5)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDO")
                        .build()
        );
    }
}
