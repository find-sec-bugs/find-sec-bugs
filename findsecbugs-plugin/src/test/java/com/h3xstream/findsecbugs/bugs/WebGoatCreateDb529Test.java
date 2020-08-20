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
package com.h3xstream.findsecbugs.bugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class WebGoatCreateDb529Test extends BaseDetectorTest {

    /**
     * The sample class contains three SQL queries executeUpdate().
     * All three are clear false positives.
     * The tougher case to eliminate is the third where an array with constant values is used.
     *
     * @throws Exception
     */
    @Test
    public void avoidSqlInjectionWithArrayUsage() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/bugs/WebGoatCreateDb529.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //No SQL injections should be trigger from this sample
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JDBC") //
                        .inClass("WebGoatCreateDb529") //
                        .build()
        );
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION") //
                        .inClass("WebGoatCreateDb529") //
                        .build()
        );
    }

}
