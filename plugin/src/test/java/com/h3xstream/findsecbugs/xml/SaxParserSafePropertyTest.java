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
package com.h3xstream.findsecbugs.xml;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class SaxParserSafePropertyTest extends BaseDetectorTest {

    @Test
    public void detectUnsafeNoSpecialSettings() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafeProperty")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .inClass("SaxParserSafeProperty").inMethod("unsafeNoSpecialSettings").atLine(26)
                        .build()
        );


        //Should not trigger the other XXE patterns
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLREADER")
                        .inClass("SaxParserSafeProperty")
                        .build()
        );
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("SaxParserSafeProperty")
                        .build()
        );
    }

    @Test
    public void avoidFalsePositiveOnSafeCases() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafeProperty")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .inClass("SaxParserSafeProperty").inMethod("safeIgnoredDtdDisable")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .inClass("SaxParserSafeProperty").inMethod("safeSecureProcessing")
                        .build()
        );

        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .inClass("SaxParserSafeProperty").inMethod("safeManualConfiguration")
                        .build()
        );



        //Only one bug should be trigger
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .inClass("SaxParserSafeProperty")
                        .build()
        );

    }
}
