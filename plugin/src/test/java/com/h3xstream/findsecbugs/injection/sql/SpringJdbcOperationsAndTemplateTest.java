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


public class SpringJdbcOperationsAndTemplateTest extends BaseDetectorTest {

    @Test
    public void detectBasicSpringJdbcInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertion on the 4 basic test
        for(String unsafeMethod : Arrays.asList("query1","query2","query3","query4")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_SPRING_JDBC")
                            .inClass("SpringJdbcTemplate")
                            .inMethod(unsafeMethod)
                            .build()
            );

            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_SPRING_JDBC")
                            .inClass("SpringJdbcOperations")
                            .inMethod(unsafeMethod)
                            .build()
            );
        }
    }

    @Test
    public void detectExecute() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryExecute")
                        .build()
        );
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryExecute")
                        .build()
        );
    }

    @Test
    public void detectBatchUpdate() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(8)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryBatchUpdate")
                        .build()
        );
        verify(reporter, times(8)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryBatchUpdate")
                        .build()
        );
    }

    @Test
    public void detectQueryForObject() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(8)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryForObject")
                        .build()
        );
        verify(reporter, times(8)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryForObject")
                        .build()
        );
    }

    @Test
    public void detectQuery() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(12)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("querySamples")
                        .build()
        );
        verify(reporter, times(12)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("querySamples")
                        .build()
        );
    }

    @Test
    public void detectQueryForList() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(6)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryForList")
                        .build()
        );
        verify(reporter, times(6)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryForList")
                        .build()
        );
    }

    @Test
    public void detectQueryForMap() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryForMap")
                        .build()
        );
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryForMap")
                        .build()
        );
    }

    @Test
    public void detectQueryForRowSet() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryForRowSet")
                        .build()
        );
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryForRowSet")
                        .build()
        );
    }

    @Test
    public void detectQueryInt() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryForInt")
                        .build()
        );
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryForInt")
                        .build()
        );
    }

    @Test
    public void detectQueryLong() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SpringJdbcOperations"),
                getClassFilePath("testcode/sqli/SpringJdbcTemplate")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Exact match of the number of vulnerability rise
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcTemplate")
                        .inMethod("queryForLong")
                        .build()
        );
        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_SPRING_JDBC")
                        .inClass("SpringJdbcOperations")
                        .inMethod("queryForLong")
                        .build()
        );
    }
}
