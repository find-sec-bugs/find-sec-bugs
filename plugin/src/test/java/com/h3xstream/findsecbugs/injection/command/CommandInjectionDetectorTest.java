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
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CommandInjectionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommandInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/CommandInjection")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        List<Integer> lines = Arrays.asList(22, 24, 29, 33, 45);
        
        //Assertions
        for (Integer line : lines) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").atLine(line)
                            .withPriority("Medium")
                            .build()
            );
        }
        
        verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").atLine(73)
                            .withPriority("High")
                            .build()
        );
        
        verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").atLine(57)
                            .withPriority("Low")
                            .build()
        );

        verify(reporter, times(lines.size())).doReportBug(
                bugDefinition().bugType("COMMAND_INJECTION").withPriority("Medium").build());
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("COMMAND_INJECTION").withPriority("High").build());
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("COMMAND_INJECTION").withPriority("Low").build());
    }
}
