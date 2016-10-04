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
package com.h3xstream.findsecbugs.scala;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScalaXssDetectorTest extends BaseDetectorTest {

    @Test
    public void detectXssInController() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_xss.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        // Test the MVC API checks
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_XSS_MVC_API")
                        .inClass("XssController").inMethod("vulnerable1").atLine(9)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_XSS_MVC_API")
                        .inClass("XssController").inMethod("vulnerable2").atLine(14)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_XSS_MVC_API")
                        .inClass("XssController").inMethod("vulnerable3").atLine(18)
                        .build()
        );

        // Test the Twirl template engine checks
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_XSS_TWIRL")
                        .inClass("XssController").inMethod("vulnerable4").atLine(22)
                        .build()
        );

        //XSS in xssString.scala.html
        verify(reporter,times(1)).doReportBug(
                bugDefinition()
                        .bugType("SCALA_XSS_TWIRL")
                        .inClass("views.html.xssString_Scope0$xssString")
                        //.atLine(31)
                        .build()
        );


        //Assertions for safe calls and false positives

        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SCALA_XSS_MVC_API")
                        .inClass("XssController").inMethod("variousSafe")
                        .build()
        );
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("SCALA_XSS_TWIRL")
                        .inClass("XssController").inMethod("variousSafe")
                        .build()
        );


        // TODO: Verify the .scala.html files
        //verify(reporter).doReportBug(
        //        bugDefinition()
        //                .bugType("SCALA_XSS_TWIRL")
        //                .inClass("xssString").atLine(4)
        //                .build()
        //);
    }
}
