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
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class XmlStreamReaderDetectorTest extends BaseDetectorTest {

    @Test
    public void detectXxeWithXMLStreamReader() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xmlinputfactory/XMLStreamReaderVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("XMLStreamReaderVulnerable")
                        .inMethod("parseXMLdefaultValue")
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("XMLStreamReaderVulnerable")
                        .inMethod("parseXMLwithWrongFlag")
                        .build()
        );
    }



    @Test
    public void avoidFPWithXMLStreamReader() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xmlinputfactory/XMLStreamReaderSafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .build()
        );
    }



    @Test
    public void detectXxeWithXMLEventReader() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xmlinputfactory/XMLEventReaderVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("XMLEventReaderVulnerable")
                        .inMethod("parseFile")
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("XMLEventReaderVulnerable")
                        .build()
        );
    }



    @Test
    public void avoidFPWithXMLEventReader() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xmlinputfactory/XMLEventReaderSafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("XMLEventReaderSafe")
                        .build()
        );
    }




    @Test
    public void detectXxeWithFilteredReader() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xmlinputfactory/FilteredReaderVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("FilteredReaderVulnerable")
                        .inMethod("parseFile")
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("FilteredReaderVulnerable")
                        .build()
        );
    }



    @Test
    public void avoidFPWithFilteredReader() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xmlinputfactory/FilteredReaderSafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLSTREAMREADER")
                        .inClass("FilteredReaderSafe")
                        .build()
        );
    }
}