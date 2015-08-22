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
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testng.annotations.Test;

public class CommandInjectionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommandInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/command/CommandInjection"),
                getClassFilePath("testcode/command/MoreMethods")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        List<Integer> linesMedium = Arrays.asList(22, 24, 29, 33, 45);
        List<Integer> linesHigh = Arrays.asList(73, 77, 89, 101, 111, 116, 125);
        List<Integer> linesLow = Arrays.asList(57, 81, 121, 126);
        
        //Assertions
        for (Integer line : linesMedium) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").atLine(line)
                            .withPriority("Medium")
                            .build()
            );
        }
        
        for (Integer line : linesHigh) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").atLine(line)
                            .withPriority("High")
                            .build()
            );
        }
        
        for (Integer line : linesLow) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").atLine(line)
                            .withPriority("Low")
                            .build()
            );
        }
        
        verify(reporter).doReportBug(
            bugDefinition()
                .bugType("COMMAND_INJECTION")
                .inClass("MoreMethods").atLine(16)
                .withPriority("High")
                .build()
        );
        
        verify(reporter, times(linesMedium.size())).doReportBug(
                bugDefinition().bugType("COMMAND_INJECTION").withPriority("Medium").build());
        verify(reporter, times(linesHigh.size() + 1)).doReportBug(
                bugDefinition().bugType("COMMAND_INJECTION").withPriority("High").build());
        verify(reporter, times(linesLow.size())).doReportBug(
                bugDefinition().bugType("COMMAND_INJECTION").withPriority("Low").build());
    }
}
