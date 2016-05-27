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

public class JdbcInjectionSourceTest extends BaseDetectorTest {

    @Test
    public void detectBasicJdbcInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/Jdbc")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion on the 4 basic test
        for (String unsafeMethod : Arrays.asList("query1", "query2", "query3", "query4")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_JDBC")
                            .inClass("Jdbc")
                            .inMethod(unsafeMethod)
                            .build()
            );
        }
    }

    @Test
    public void detectExecuteQuery() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/Jdbc")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion
        verify(reporter,times(5)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("Jdbc")
                        .inMethod("executeQuerySamples")
                        .build()
        );
    }


    @Test
    public void detectExecuteUpdate() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/Jdbc")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion
        verify(reporter,times(4)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("Jdbc")
                        .inMethod("executeUpdateSamples")
                        .build()
        );
    }

    @Test
    public void detectExecuteExecuteLargeUpdate() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/Jdbc")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion
        verify(reporter,times(4)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("Jdbc")
                        .inMethod("executeExecuteLargeUpdateSamples")
                        .build()
        );
    }


    @Test
    public void detectExecutePrepareCall() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/Jdbc")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion
        verify(reporter,times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("Jdbc")
                        .inMethod("executePrepareCallSamples")
                        .build()
        );
    }

    @Test
    public void detectPrepareStatement() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/Jdbc")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion
        verify(reporter,times(6)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("Jdbc")
                        .inMethod("prepareStatementSamples")
                        .build()
        );
    }


    @Test
    public void detectNativeSqlAndOther() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/Jdbc")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion
        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC")
                        .inClass("Jdbc")
                        .inMethod("otherSamples")
                        .build()
        );
    }
}
