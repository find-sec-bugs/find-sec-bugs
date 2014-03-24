/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs.xxe;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SaxParserXxeDetectorTest extends BaseDetectorTest {

    @Test
    public void detectXxe() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .inClass("SaxParserVulnerable").inMethod("receiveXMLStream").atLine(22)
                        .build()
        );
    }

    @Test
    public void safeWithUseOfPrivilegeExceptionAction() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafePrivilegedExceptionAction"),
                getClassFilePath("testcode/xxe/SaxParserSafePrivilegedExceptionAction$1")
        };

        //Run the analysis
        EasyBugReporter reporter = Mockito.spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .build()
        );
    }

    @Test
    public void safeWithUseOfEntityResolver() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafeEntityResolver"),
                getClassFilePath("testcode/xxe/SaxParserSafeEntityResolver$CustomResolver")
        };

        //Run the analysis
        EasyBugReporter reporter = Mockito.spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .build()
        );
    }



    @Test
    public void detectXxeFromDocumentBuilder() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/DocumentBuilderVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .inClass("DocumentBuilderVulnerable").inMethod("receiveXMLStream").atLine(18)
                        .build()
        );
    }

}
