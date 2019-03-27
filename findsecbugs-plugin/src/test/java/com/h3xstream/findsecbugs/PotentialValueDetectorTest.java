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
package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PotentialValueDetectorTest extends BaseDetectorTest {


    @Test
    public void detectHardcodePassword1() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/potential/PotentialHardcodePassword")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(16, 29, 30, /*31,*/ 32)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("PotentialHardcodePassword").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(4)).doReportBug(
                bugDefinition()
                        .bugType("HARD_CODE_PASSWORD")
                        .inClass("PotentialHardcodePassword")
                        .build()
        );

    }



    @Test
    public void detectDes1() throws Exception {
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/potential/PotentialAlgorithm")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(10)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("DES_USAGE")
                            .inClass("PotentialAlgorithm").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(1)).doReportBug(
                bugDefinition()
                        .bugType("DES_USAGE")
                        .inClass("PotentialAlgorithm")
                        .build()
        );

    }
}