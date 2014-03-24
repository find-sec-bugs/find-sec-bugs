/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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

        //Message Digest
        for(int line : Arrays.asList(12,16,20)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST")
                            .inClass("WeakMessageDigest").inMethod("main").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(3)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST")
                        .inClass("WeakMessageDigest").inMethod("main")
                        .build()
        );
    }

    @Test
    public void detectWeakDigestApache() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/WeakMessageDigest")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Message Digest
        int l = 37;
        for(int line : Arrays.asList(l++,l++,l++,l++, l+=2,l++,l++, l+=2,l++,l++)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST")
                            .inClass("WeakMessageDigest").inMethod("apacheApiVariations").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(10)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST")
                        .inClass("WeakMessageDigest").inMethod("apacheApiVariations")
                        .build()
        );
    }
}
