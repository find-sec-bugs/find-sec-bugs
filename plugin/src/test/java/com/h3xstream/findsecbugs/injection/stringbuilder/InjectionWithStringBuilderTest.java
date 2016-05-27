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
package com.h3xstream.findsecbugs.injection.stringbuilder;


import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testng.annotations.Test;

public class InjectionWithStringBuilderTest extends BaseDetectorTest {

    @Test
    public void detectSuspiciousCase() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/stringbuilder/StringBuilderSuspicious")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryTaintedValueInConstructor").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryTaintedValueInAppendMethod1").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryTaintedValueInAppendMethod2").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryUnknownSource1").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryUnknownSource2").build()
        );

        verify(reporter, times(1)).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryUnknownTransformation").build()
        );

        verify(reporter, times(6)).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .build()
        );
    }



    @Test
    public void avoidFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/stringbuilder/StringBuilderFalsePositive")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, never()).doReportBug(bugDefinition().bugType("SQL_INJECTION_JPA").build());
        verify(reporter,never()).doReportBug(bugDefinition().bugType("SQL_INJECTION_JDO").build());
        verify(reporter,never()).doReportBug(bugDefinition().bugType("SQL_INJECTION_HIBERNATE").build());
    }

}
