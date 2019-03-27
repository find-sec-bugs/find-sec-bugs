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
import edu.umd.cs.findbugs.BugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class IntuitiveHardcodePasswordDetectorTest extends BaseDetectorTest {


    @Test
    public void detectHardCodePasswordsAndKeys() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(false);
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(false);


        String[] files = {
                getClassFilePath("testcode/password/customapi/HardCodeSample"),
                getClassFilePath("testcode/password/customapi/Vault"),
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(7, 12, 17, 22)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("HardCodeSample").atLine(line)
                            .build()
            );
        }

        verify(reporter, times(4)).doReportBug(bugDefinition().bugType("HARD_CODE_PASSWORD").build());
    }
}
