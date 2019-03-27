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
package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OgnlInjectionDetectorTest  extends BaseDetectorTest {

//    @BeforeClass
//    public void setUp() {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);
//    }

    @Test
    public void detectOgnlReflectionProvider() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/script/ognl/OgnlReflectionProviderSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,times(10)).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("OgnlReflectionProviderSample")
                        .inMethod("unsafeOgnlReflectionProvider")
                        .build()
        );

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("OgnlReflectionProviderSample")
                        .inMethod("safeOgnlReflectionProvider")
                        .build()
        );
    }

    @Test
    public void detectOgnlUtil() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/script/ognl/OgnlUtilSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,times(11)).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("OgnlUtilSample")
                        .inMethod("unsafeOgnlUtil")
                        .build()
        );

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("OgnlUtilSample")
                        .inMethod("safeOgnlUtil")
                        .build()
        );
    }


    @Test
    public void detectTextParser() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/script/ognl/TextParserSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,times(7)).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("TextParserSample")
                        .inMethod("unsafeTextParseUtil")
                        .build()
        );

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("TextParserSample")
                        .inMethod("safeTextParseUtil")
                        .build()
        );

        for(Integer line : Arrays.asList(11,21)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("OGNL_INJECTION")
                            .inClass("TextParserSample")
                            .atLine(line)
                            .build()
            );
        }

        verify(reporter,times(7+2)).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("TextParserSample")
                        .build());
    }


    @Test
    public void detectValueStack() throws Exception {

//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/script/ognl/ValueStackSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(8,16)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("OGNL_INJECTION")
                            .inClass("ValueStackSample")
                            .atLine(line)
                            .build()
            );
        }

        verify(reporter,times(9)).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("ValueStackSample")
                        .build());
    }



    @Test
    public void detectTaintedAndInjection() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/script/ognl/StrutsTaintedApi")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("StrutsTaintedApi")
                        .withPriority("High")
                        .atLine(10)
                        .build()
        );

        //Only 1 bug expected
        verify(reporter,times(1)).doReportBug(
                bugDefinition()
                        .bugType("OGNL_INJECTION")
                        .inClass("StrutsTaintedApi")
                        .build()
        );
    }
}
