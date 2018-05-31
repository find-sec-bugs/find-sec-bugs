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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class TransformerFactoryDetectorTest extends BaseDetectorTest {

    @BeforeClass
    public void traceCalls() {
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
    }

    @Test
    public void detectXxe_TransformerFactory() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/transformerfactory/TransformerFactoryVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        List<String> vulnerableMethods = Arrays.asList("parseXMLdefaultValue", "parseXMLDoS", "parseXMLOneLiner",
                "parseXMLWithXslt", "parseXMLWithMissingAttributeDtd", "parseXMLWithWrongFlag1", "parseXMLWithWrongFlag2");
        List<String> vulnerableXsltMethods = Arrays.asList("parseXMLdefaultValue", "parseXMLDoS", "parseXMLOneLiner",
                "parseXMLWithXslt", "parseXMLWithMissingAttributeStylesheet", "parseXMLWithWrongFlag1", "parseXMLWithWrongFlag2");

        for (String method : vulnerableMethods) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("XXE_DTD_TRANSFORM_FACTORY")
                            .inClass("TransformerFactoryVulnerable").inMethod(method)
                            .build()
            );
        }

        for (String method : vulnerableXsltMethods) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("XXE_XSLT_TRANSFORM_FACTORY")
                            .inClass("TransformerFactoryVulnerable").inMethod(method)
                            .build()
            );
        }

        // We do not want to spam users with multiple report of the "same" vulnerability
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLREADER")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .build()
        );
    }

    @Test
    public void avoidFalsePositive_TransformerFactory() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/transformerfactory/TransformerFactorySafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DTD_TRANSFORM_FACTORY")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XSLT_TRANSFORM_FACTORY")
                        .build()
        );

        // We do not want to spam users with multiple report of the "same" vulnerability
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLREADER")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .build()
        );
    }


    @Test
    public void detectVariation_SaxTransformerFactory() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/transformerfactory/SaxTransformerFactoryVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_DTD_TRANSFORM_FACTORY")
                        .inClass("SaxTransformerFactoryVulnerable").inMethod("parseXML")
                        .build()
        );


        // We do not want to spam users with multiple report of the "same" vulnerability
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_SAXPARSER")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLREADER")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .build()
        );
    }
}
