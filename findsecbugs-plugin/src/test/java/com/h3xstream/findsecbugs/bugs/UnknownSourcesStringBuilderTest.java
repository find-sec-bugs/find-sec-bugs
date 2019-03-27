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
package com.h3xstream.findsecbugs.bugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class UnknownSourcesStringBuilderTest extends BaseDetectorTest {

    @Test
    public void detectUnknownSourceField() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/juliet/CWE90_LDAP_Injection__File_68b")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("LDAP_INJECTION") //
                        .inClass("CWE90_LDAP_Injection__File_68b").inMethod("goodG2BSink").atLine(81)
                        .causeBySource("testcode/juliet/CWE90_LDAP_Injection__File_68a.data")
                        .build()
        );
    }


    @Test
    public void detectUnknownSourceReadLine() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/juliet/CWE113_HTTP_Response_Splitting__File_addCookieServlet_12")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("HTTP_RESPONSE_SPLITTING") //
                        .inClass("CWE113_HTTP_Response_Splitting__File_addCookieServlet_12").inMethod("bad").atLine(91)
                        .causeBySource("java/io/BufferedReader.readLine()Ljava/lang/String;")
                        .build()
        );

    }

}
