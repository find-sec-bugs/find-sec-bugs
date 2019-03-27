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

import static org.mockito.Mockito.*;

public class WeakMessageDigestDetectorTest extends BaseDetectorTest {

    @Test
    public void detectWeakDigest() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/WeakMessageDigest")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Message Digest

        for(int line : Arrays.asList(12,16)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST_MD5")
                            .inClass("WeakMessageDigest").inMethod("main").atLine(line)
                            .build()
            );
        }
        //SHA1
        for(int line : Arrays.asList(20,24,28)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST_SHA1")
                            .inClass("WeakMessageDigest").inMethod("main").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_MD5")
                        .inClass("WeakMessageDigest").inMethod("main")
                        .build()
        );

        verify(reporter,times(3)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_SHA1")
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
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Message Digest
        for(int line = 45; line<=52;line++) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST_MD5")
                            .inClass("WeakMessageDigest").inMethod("apacheApiVariations").atLine(line)
                            .build()
            );
        }

        for(int line = 54; line<=61;line++) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST_SHA1")
                            .inClass("WeakMessageDigest").inMethod("apacheApiVariations").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(8)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_MD5")
                        .inClass("WeakMessageDigest").inMethod("apacheApiVariations")
                        .build()
        );
        verify(reporter,times(8)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_SHA1")
                        .inClass("WeakMessageDigest").inMethod("apacheApiVariations")
                        .build()
        );
    }

    @Test
    public void detectWeakDigestAdditionalSignatures() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/WeakMessageDigestAdditionalSig")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //MD5 Assertions
        for(int line = 12; line <= 20; line++) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST_MD5")
                            .inClass("WeakMessageDigestAdditionalSig").inMethod("weakDigestMoreSig").atLine(line)
                            .build()
            );
        }
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_MD5")
                        .inClass("WeakMessageDigestAdditionalSig").inMethod("weakDigestMoreSig").atLine(30)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_MD5")
                        .inClass("WeakMessageDigestAdditionalSig").inMethod("weakDigestMoreSig").atLine(31)
                        .build()
        );

        //SHA-1 Assertions
        for(int line = 21; line <= 26; line++) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("WEAK_MESSAGE_DIGEST_SHA1")
                            .inClass("WeakMessageDigestAdditionalSig").inMethod("weakDigestMoreSig").atLine(line)
                            .build()
            );
        }
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_SHA1")
                        .inClass("WeakMessageDigestAdditionalSig").inMethod("weakDigestMoreSig").atLine(32)
                        .build()
        );

        verify(reporter,times(11)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_MD5")
                        .inClass("WeakMessageDigestAdditionalSig").inMethod("weakDigestMoreSig")
                        .build()
        );
        verify(reporter,times(7)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST_SHA1")
                        .inClass("WeakMessageDigestAdditionalSig").inMethod("weakDigestMoreSig")
                        .build()
        );
    }
}
