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
 * Regression test for https://github.com/find-sec-bugs/find-sec-bugs/issues/764
 *
 * SQL_INJECTION_JDBC must be reported when a tainted value is appended as the
 * second, chained argument of a StringBuilder.append() call (for example
 * sql.append(',').append(tainted)), not only when the tainted value is appended
 * first or through a separate statement.
 */
public class SqlInjectionStringBuilderChained764Test extends BaseDetectorTest {

    @Test
    public void detectChainedAppendSqlInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/SqlInjectionStringBuilderChained764")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (String unsafeMethod : Arrays.asList(
                "chainedAppendTaintedFirst",
                "chainedAppendTaintedSecond",
                "usesIteratorOfIntegersWithChainedAppendItemFirst",
                "usesIteratorOfIntegersWithChainedAppendItemSecond",
                "usesIteratorOfIntegersWithSeparateAppendItemFirst",
                "usesIteratorOfIntegersWithSeparateAppendItemSecond")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_JDBC")
                            .inClass("SqlInjectionStringBuilderChained764")
                            .inMethod(unsafeMethod)
                            .build()
            );
        }
    }
}
