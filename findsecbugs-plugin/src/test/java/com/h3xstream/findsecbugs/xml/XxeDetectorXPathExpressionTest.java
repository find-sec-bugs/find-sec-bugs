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
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * This class test specific test case for the XPathExpression.
 * These test cases are cover by the XxeDetector which looks for the initialisation of DocumentBuilderFactory.
 */
public class XxeDetectorXPathExpressionTest extends BaseDetectorTest {

    @Test
    public void detectXxeVulnerabilityXPath() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xpathexpression/XPathExpressionVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);


        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_DOCUMENT")
                        .inClass("XPathExpressionVulnerable").inMethod("unsafe1").atLine(38)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_XPATH")
                        .inClass("XPathExpressionVulnerable").inMethod("unsafe2").atLine(51)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE_XPATH")
                        .inClass("XPathExpressionVulnerable").inMethod("unsafe3").atLine(64)
                        .build()
        );


        //Other types of XXE should not be raise..

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XMLREADER")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_DTD_TRANSFORM_FACTORY")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XSLT_TRANSFORM_FACTORY")
                        .build()
        );
    }



    @Test
    public void avoidFPXxeVulnerabilityXPath() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/xpathexpression/XPathExpressionSafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("XXE_XPATH")
                        .build()
        );
    }
}
