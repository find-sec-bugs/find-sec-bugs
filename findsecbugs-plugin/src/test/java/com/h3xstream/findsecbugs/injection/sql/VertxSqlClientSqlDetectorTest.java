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

import static org.mockito.Mockito.*;

public class VertxSqlClientSqlDetectorTest extends BaseDetectorTest {

    @Test
    public void detectInjection() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/VertxSqlClient")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection1").atLine(9)
                        .build()
        );

        //Only 1 bug is expected
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection1")
                        .build()
        );


        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection2").atLine(13)
                        .build()
        );

        //Only 1 bug is expected
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection2")
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection3").atLine(17)
                        .build()
        );

        //Only 1 bug is expected
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection3")
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection4").atLine(21)
                        .build()
        );

        //Only 1 bug is expected
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("injection4")
                        .build()
        );

        // False positives

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("falsePositive1")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_VERTX")
                        .inClass("VertxSqlClient")
                        .inMethod("falsePositive2")
                        .build()
        );
    }
}
