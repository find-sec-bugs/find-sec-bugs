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
package com.h3xstream.findsecbugs.password;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HardcodePasswordInMapDetectorTest extends BaseDetectorTest {

    @Test
    public void detectHardCodeCredentialsVariousMap() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);//setDebugPrintInvocationVisited(true);
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/password/VariousMap")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(14, 20, 26, 32, 38)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("VariousMap").atLine(line)
                            .build()
            );
        }

        //More than two occurrence == false positive
        verify(reporter, times(5)).doReportBug(
                bugDefinition().bugType("HARD_CODE_PASSWORD").build());
    }

    @Test
    public void detectHardCodeCredentials() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);//setDebugPrintInvocationVisited(true);
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/password/JndiProperties")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(14, 19,30)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("JndiProperties").atLine(line)
                            .build()
            );
        }

        //More than two occurrence == false positive
        verify(reporter, times(3)).doReportBug(
                bugDefinition().bugType("HARD_CODE_PASSWORD").build());
    }
}
