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
package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DesUsageDetectorTest extends BaseDetectorTest {

    @Test
    public void detectDesCipher() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/BlockCipherList")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(20, 21, 22, 23, 24, 25, 26, 27, 33, 34)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("DES_USAGE")
                            .inClass("BlockCipherList")
                            .inMethod("main")
                            .atLine(line)
                            .build()
            );
        }

        //Nothing more than the previous 10
        verify(reporter, times(10)).doReportBug(
                bugDefinition()
                        .bugType("DES_USAGE")
                        .inClass("BlockCipherList")
                        .build()
        );
    }

    @Test
    public void detectDesKeyGeneration() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/DesKeyGeneration")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(11,12,13,14,15,16,17,18,19,20)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("DES_USAGE")
                            .inClass("DesKeyGeneration")
                            .inMethod("weakDesKeyGenerator")
                            .atLine(line)
                            .build()
            );
        }

        //Nothing more than the previous 4
        verify(reporter, times(10)).doReportBug(
                bugDefinition()
                        .bugType("DES_USAGE")
                        .inClass("DesKeyGeneration")
                        .build()
        );
    }
}
