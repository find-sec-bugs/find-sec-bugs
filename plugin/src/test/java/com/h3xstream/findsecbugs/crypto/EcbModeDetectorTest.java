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

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class EcbModeDetectorTest extends BaseDetectorTest {


    @Test
    public void detectEcbMode() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/BlockCipherList")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        for (Integer line : Arrays.asList(18, 19, 22, 23, 26, 27, 34)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("ECB_MODE")
                            .inClass("BlockCipherList")
                            .inMethod("main")
                            .atLine(line)
                            .build()
            );
        }

        //The count make sure no other bug are detect
        verify(reporter, times(7)).doReportBug(
                bugDefinition()
                        .bugType("ECB_MODE")
                        .inClass("BlockCipherList")
                        .inMethod("main")
                        .build());
    }

}
