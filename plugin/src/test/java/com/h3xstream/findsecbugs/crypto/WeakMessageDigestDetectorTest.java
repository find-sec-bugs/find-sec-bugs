/**
 * Find Security Bugs
 * Copyright (c) 2012, Philippe Arteau, All rights reserved.
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

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class WeakMessageDigestDetectorTest extends BaseDetectorTest {

    @Test
    public void detectWeakDigest() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/WeakMessageDigest")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //MD2 usage
        verify(reporter, atLeastOnce()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST")
                        .inClass("WeakMessageDigest").inMethod("main").atLine(16)
                        .build()
        );

        //MD5 usage
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST")
                        .inClass("WeakMessageDigest").inMethod("main").atLine(16)
                        .build()
        );
    }
}
