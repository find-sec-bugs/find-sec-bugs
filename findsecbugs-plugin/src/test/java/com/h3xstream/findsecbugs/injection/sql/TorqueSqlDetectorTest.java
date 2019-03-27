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
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Those tests are identical to TurbineSqlDetectorTest.
 */
public class TorqueSqlDetectorTest extends BaseDetectorTest {

    @Test
    public void detectInjection() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/turbine/TorqueSql")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : range(8,13)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_TURBINE")
                            .inClass("TorqueSql")
                            .inMethod("injection1").atLine(line)
                            .build()
            );
        }

        //Only 6 bugs are expected
        verify(reporter, times(6)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_TURBINE")
                        .inClass("TorqueSql")
                        .inMethod("injection1")
                        .build()
        );


        for (Integer line : range(17,22)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_TURBINE")
                            .inClass("TorqueSql")
                            .inMethod("injection2").atLine(line)
                            .build()
            );
        }

        //Only 6 bugs are expected
        verify(reporter, times(6)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_TURBINE")
                        .inClass("TorqueSql")
                        .inMethod("injection2")
                        .build()
        );


        //Only 6 bugs are expected
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_TURBINE")
                        .inClass("TorqueSql")
                        .inMethod("falsePositive")
                        .build()
        );
    }
}
