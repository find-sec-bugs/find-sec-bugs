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
import java.util.Arrays;
import java.util.List;
import org.testng.annotations.Test;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ScalaSqlInjectionAnormDetectorTest extends BaseDetectorTest {

    /**
     * Test the API coverage for SqlInjectionDetector (loadConfiguredSinks("sql-scala-anorm.txt", "SCALA_SQL_INJECTION_ANORM);)
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

        //Assertions for single bug methods
        List<String> vulnerableMethods = Arrays.asList("vulnerableAnorm1", "vulnerableAnorm3", "vulnerableAnorm5",
                "vulnerableAnorm7", "vulnerableAnorm9");

        for (String method : vulnerableMethods) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SCALA_SQL_INJECTION_ANORM")
                            .inClass("SqlController").inMethod(method)
                            .build()
            );
        }

        // Assertions for multiple bugs methods
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_ANORM")
                        .inClass("SqlController").inMethod("vulnerableAnorm2")
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_ANORM")
                        .inClass("SqlController").inMethod("vulnerableAnorm4")
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_ANORM")
                        .inClass("SqlController").inMethod("vulnerableAnorm6")
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_ANORM")
                        .inClass("SqlController").inMethod("vulnerableAnorm8")
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_ANORM")
                        .inClass("SqlController").inMethod("vulnerableAnorm10")
                        .build()
        );

        verify(reporter, times(6)).doReportBug(
                bugDefinition()
                        .bugType("SCALA_SQL_INJECTION_ANORM")
                        .inClass("SqlController").inMethod("vulnerableAnorm11")
                        .build()
        );


        //Assertions for safe calls and false positives
        List<String> safeMethods = Arrays.asList("variousSafeAnormSelect", "variousSafeAnormUpdate",
                "variousSafeAnormInsert", "variousSafeAnormBatch");

        for (String method : safeMethods) {
            verify(reporter,never()).doReportBug(
                    bugDefinition()
                            .bugType("SCALA_SQL_INJECTION_ANORM")
                            .inClass("SqlController").inMethod(method)
                            .build()
            );
        }
    }
}
