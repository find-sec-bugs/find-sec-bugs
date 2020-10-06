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
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class StringSubstitutorTest extends BaseDetectorTest {

    @Test
    public void stringSubstitutorUnsafe() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/strsubstitutor/StringSubstitutorUnsafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,times(2)).doReportBug(bugDefinition()
                .bugType("SQL_INJECTION_SPRING_JDBC").inClass("StringSubstitutorUnsafe").inMethod("addUser1Unsafe")
                .build());
        verify(reporter).doReportBug(bugDefinition()
                .bugType("SQL_INJECTION_SPRING_JDBC").inClass("StringSubstitutorUnsafe").inMethod("addUser2Unsafe")
                .build());
        verify(reporter,times(2)).doReportBug(bugDefinition()
                .bugType("SQL_INJECTION_SPRING_JDBC").inClass("StringSubstitutorUnsafe").inMethod("addUser3Unsafe")
                .build());

        //Total number is 5
        verify(reporter,times(5)).doReportBug(bugDefinition()
                .bugType("SQL_INJECTION_SPRING_JDBC").inClass("StringSubstitutorUnsafe").build());
    }


    @Test
    public void stringSubstitutorSafe() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/strsubstitutor/StringSubstitutorSafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(bugDefinition()
                .bugType("SQL_INJECTION_SPRING_JDBC").inClass("StringSubstitutorSafe")
                .build());
    }

}
