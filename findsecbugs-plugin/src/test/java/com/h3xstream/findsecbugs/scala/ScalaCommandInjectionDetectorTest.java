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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ScalaCommandInjectionDetectorTest extends BaseDetectorTest {

    /**
     * Test the API coverage for CommandInjectionDetector (loadConfiguredSinks("command-scala.txt", "SCALA_COMMAND_INJECTION");)
     * @throws Exception
     */
    @Test
    public void detectCommandInjection() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(false);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_command_injection.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("vulnerable1").atLine(13)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("vulnerable2").atLine(19)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("vulnerable3").atLine(25)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("vulnerable4").atLine(31)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("vulnerable5").atLine(36)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("vulnerable6").atLine(41)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("extendedVulnerable1").atLine(46)
                        .build()
        );


        List<Integer> lineVariousMethod = Arrays.asList(51, /**/ 53, 54, 55, /**/ 58, 60, 61, /**/ 64, 66, 67,
            /**/ 70, 71, 72, /**/ 75, /**/ 79, 80);
        for(int line : lineVariousMethod) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SCALA_COMMAND_INJECTION")
                            .inClass("CommandController").inMethod("various").atLine(line)
                            .build()
            );
        }

        List<Integer> lineFalsePositives = Arrays.asList(56,59,62, 65);
        for(int line : lineFalsePositives) {
            verify(reporter,never()).doReportBug(
                    bugDefinition()
                            .bugType("SCALA_COMMAND_INJECTION")
                            .inClass("CommandController").inMethod("various").atLine(line)
                            .build()
            );
        }
        verify(reporter, times(lineVariousMethod.size())).doReportBug(
                bugDefinition()
                        .bugType("SCALA_COMMAND_INJECTION")
                        .inClass("CommandController").inMethod("various")
                        .build()
        );

        //PATH injection is typically consider a command injection because it could lead to unwanted command execution
        //Currently it is catch as Path traversal
        //
        // Process(Seq("ls"),new File(value),("extra","1234")).run()

        List<Integer> linePathTraversal = Arrays.asList(59, 65);
        for(int line : linePathTraversal) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("PATH_TRAVERSAL_IN")
                            .inClass("CommandController").inMethod("various").atLine(line)
                            .build()
            );
        }
    }
}
