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

import edu.umd.cs.findbugs.BugReporter;
import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class WeakTrustManagerDetectorTest extends BaseDetectorTest {


    @Test
    public void detectWeakTrustManager() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/trustmanager/WeakTrustManager"),
                getClassFilePath("testcode/trustmanager/WeakTrustManager$TrustManagerInnerClass"),
                getClassFilePath("testcode/trustmanager/WeakTrustManager$1"),

                //The following should not trigger any bug
                getClassFilePath("testcode/trustmanager/WeakTrustManager$FakeImpl"),
                getClassFilePath("testcode/trustmanager/KeyStoresTrustManager")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

        //Anonymous class

        verify(reporter, atLeastOnce()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$1").inMethod("getAcceptedIssuers")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$1").inMethod("checkServerTrusted")
                        .build()
        );

        //Inner class
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$TrustManagerInnerClass").inMethod("checkServerTrusted")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$TrustManagerInnerClass").inMethod("getAcceptedIssuers")
                        .build()
        );

        //The KeyStoresTrustManager impl. should not trigger any report
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("KeyStoresTrustManager")
                        .build()
        );

        //The FakeImpl should not trigger any report (doesn't implements the analysed Interface)
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("FakeImpl")
                        .build()
        );
    }
}
