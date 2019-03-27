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
import static org.mockito.Mockito.verify;

public class ScalaSqlInjectionSlickDetectorTest extends BaseDetectorTest {

    /**
     * Test the API coverage for SqlInjectionDetector (loadConfiguredSinks("sql-scala-slick.txt", "SCALA_SQL_INJECTION_SLICK);)
     * @throws Exception
     */
    @Test
    public void detectSqlInjection() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(false);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_sql_injection.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions for bugs
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_SLICK")
                        .inClass("SqlController").inMethod("vulnerableSlick1")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_SLICK")
                        .inClass("SqlController").inMethod("vulnerableSlick2")
                        .build()
        );


        //Assertions for safe calls and false positives
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_SLICK")
                        .inClass("SqlController").inMethod("variousSafeSlick1")
                        .build()
        );
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_SLICK")
                        .inClass("SqlController").inMethod("variousSafeSlick2")
                        .build()
        );
    }
}
