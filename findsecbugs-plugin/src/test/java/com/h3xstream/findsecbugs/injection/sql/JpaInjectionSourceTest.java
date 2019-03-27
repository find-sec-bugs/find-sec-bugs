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

import static org.mockito.Mockito.*;

public class JpaInjectionSourceTest extends BaseDetectorTest {

    @Test
    public void detectJpaInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/JpaSql")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JPA")
                        .inClass("JpaSql").inMethod("getUserByUsername").atLine(16)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JPA")
                        .inClass("JpaSql").inMethod("getUserByUsernameAlt2").atLine(24)
                        .build()
        );

        //Only the previous 5 cases should be marked as vulnerable
        //2 createQuery + 3 createNativeQuery detect
        verify(reporter, times(2+3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JPA")
                        .build()
        );
    }

    @Test
    public void detectJpaInjectionInNativeQuery() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/JpaSql")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //All 3 method signatures are detected

        for(Integer l : Arrays.asList(51,52,53)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SQL_INJECTION_JPA")
                            .inClass("JpaSql").inMethod("getUserWithNativeQueryUnsafe").atLine(l)
                            .build()
            );
        }

        //Check for false positive

        verify(reporter, times(3)).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JPA")
                        .inClass("JpaSql").inMethod("getUserWithNativeQueryUnsafe")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION_JPA")
                        .inClass("JpaSql").inMethod("getUserWithNativeQuerySafe")
                        .build()
        );
    }
}
