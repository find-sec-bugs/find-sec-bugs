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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DocumentBuilderSafePropertyTest  extends BaseDetectorTest {

    @Test
    public void detectUnsafeNoSpecialSettings() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/DocumentBuilderSafeProperty")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("unsafeNoSpecialSettings").atLine(33)
                        .build()
        );

        //Should not trigger the other XXE patterns
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .build()
        );
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLREADER")
                        .build()
        );
    }

    @Test
    public void avoidFalsePositiveOnSafeConfiguration() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/DocumentBuilderSafeProperty")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("safeSecureProcessing")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("safeDtdDisable")
                        .build()
        );

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("safeManualConfiguration")
                        .build()
        );

    }

    @Test
    public void detectUnsafePartialConfiguration() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/DocumentBuilderSafeProperty")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("unsafeManualConfig1").atLine(90)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("unsafeManualConfig2").atLine(102)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("unsafeManualConfig3").atLine(114)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("DocumentBuilderSafeProperty").inMethod("unsafeManualConfig4").atLine(126)
                        .build()
        );


    }
}
