/**
 * Find Security Bugs
 * Copyright (c) 2014, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class StaticIvDetectorTest extends BaseDetectorTest {

    @Test
    public void detectStaticIv() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/iv/StaticVariableIv")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter).doReportBug( //
                bugDefinition().bugType("STATIC_IV").inClass("StaticVariableIv").inMethod("encrypt").atLine(26).build()
        );

        //Only one report of this bug pattern
        verify(reporter,times(1)).doReportBug( //
                bugDefinition().bugType("STATIC_IV").inClass("StaticVariableIv").build()
        );
    }

    @Test
    public void detectConstantIv() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/iv/ConstantIv")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

        for(int line : Arrays.asList(17,36,55)) {
            verify(reporter).doReportBug( //
                    bugDefinition().bugType("STATIC_IV").inClass("ConstantIv").atLine(line).build()
            );
        }

        //Only one report of this bug pattern
        verify(reporter,times(3)).doReportBug( //
                bugDefinition().bugType("STATIC_IV").inClass("ConstantIv").build()
        );
    }

    @Test
    public void avoidFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/iv/SafeIvGeneration")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

        //Not bug should be report
        verify(reporter,never()).doReportBug( //
                bugDefinition().bugType("STATIC_IV").inClass("SafeIvGeneration").build()
        );
    }

}
