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
import static org.mockito.Mockito.verify;

/**
 * Follow-up to https://github.com/find-sec-bugs/find-sec-bugs/issues/764
 *
 * SQL_INJECTION_JDBC must be reported for the StringBuilder/StringBuffer
 * append()/insert() overloads that chained-append fix #764 did not cover:
 * append(char[]), append(char[],int,int), appendCodePoint(int),
 * insert(int,char[]), insert(int,char[],int,int), and the single-argument
 * primitive insert() overloads used as the first link of a chain.
 */
public class SqlInjectionStringBuilderConfigGapsTest extends BaseDetectorTest {

    @Test
    public void detectStringBuilderConfigGapsSqlInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SqlInjectionStringBuilderConfigGaps")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (String unsafeMethod : Arrays.asList(
                "appendCharArrayChained",
                "appendCharArrayRangeChained",
                "appendCodePointChained",
                "insertCharArrayChained",
                "insertCharArrayRangeChained",
                "chainedInsertPrimitiveTaintedSecond")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_JDBC")
                            .inClass("SqlInjectionStringBuilderConfigGaps")
                            .inMethod(unsafeMethod)
                            .build()
            );
        }
    }
}
