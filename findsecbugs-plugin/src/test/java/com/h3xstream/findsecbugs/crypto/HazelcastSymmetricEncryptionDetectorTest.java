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

import org.testng.annotations.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class HazelcastSymmetricEncryptionDetectorTest extends BaseDetectorTest {


    @Test
    public void detectHazelcastSymmetric() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/HazelcastSymmetric")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Identify the constructor
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("HAZELCAST_SYMMETRIC_ENCRYPTION")
                        .inClass("HazelcastSymmetric")
                        .inMethod("init")
                        .atLine(26)
                        .build()
        );
    }

}
