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
package com.h3xstream.findsecbugs.injection.command;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Test some additional signatures for ProcessBuilder.
 */
public class CommandInjectionDetectorAdvancedTest extends BaseDetectorTest {
    
    @Test
    public void avoidFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/command/CommandInjectionSafe"),
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("CommandInjectionSafe")
                        .build()
        );

    }

    @Test
    public void detectSuspicious() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/command/CommandInjectionSuspicious"),
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        //Assertions
        for(Integer line : Arrays.asList(11,13)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureConstructorArray").atLine(line)
                            .build()
            );
        }

        for(Integer line : Arrays.asList(20,23)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureConstructorList").atLine(line)
                            .build()
            );
        }

        for(Integer line : Arrays.asList(27,29)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureCommandMethodArray").atLine(line)
                            .build()
            );
        }

        for(Integer line : Arrays.asList(35,38)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureCommandMethodList").atLine(line)
                            .build()
            );
        }

    }

}
