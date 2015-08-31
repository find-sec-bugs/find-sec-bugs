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
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InjectionWithStringBuilderTest extends BaseDetectorTest {

    @Test
    public void detectSuspiciousCase() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/stringbuilder/StringBuilderSuspicious")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryTaintedValueInConstructor").withPriority("Medium").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryTaintedValueInAppendMethod1").withPriority("Medium").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryTaintedValueInAppendMethod2").withPriority("Medium").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryUnknownSource1").withPriority("Medium").build()
        );
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryUnknownSource2").withPriority("Medium").build()
        );
        //FIXME: This is a huge failure from the Taint analysis. Fix ASAP..
        /*
        verify(reporter).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .inMethod("queryUnknownTransformation").withPriority("Low").build()
        );
        */
        verify(reporter, times(5)).doReportBug(bugDefinition()
                        .bugType("SQL_INJECTION_JPA").inClass("StringBuilderSuspicious")
                        .withPriority("Medium").build()
        );
    }



    @Test
    public void avoidFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/stringbuilder/StringBuilderFalsePositive")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        //Currently, the false positives are still rise but with Low priority. (To avoid hiding potential critical vulnerability.)
        verify(reporter, never()).doReportBug(bugDefinition().bugType("SQL_INJECTION_JPA").withPriority("Medium").build());
        verify(reporter,never()).doReportBug(bugDefinition().bugType("SQL_INJECTION_JDO").withPriority("Medium").build());
        verify(reporter,never()).doReportBug(bugDefinition().bugType("SQL_INJECTION_HIBERNATE").withPriority("Medium").build());
    }

}
