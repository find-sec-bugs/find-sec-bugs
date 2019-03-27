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
package com.h3xstream.findsecbugs.scala;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SslDisablerDetectorTest extends BaseDetectorTest {

    @Test
    public void detectSslDisabler() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_ssl_disabler.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_HOSTNAME_VERIFIER")
                        .inClass("SecurityBypasserUsage$").inMethod("useAllHosts").atLine(5)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("SecurityBypasserUsage$").inMethod("useTrustAllManager").atLine(9)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("SecurityBypasserUsage$").inMethod("useSecurityBypasser").atLine(17)
                        .build()
        );

        verify(reporter,times(1)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_HOSTNAME_VERIFIER").inClass("SecurityBypasserUsage$").build()
        );
        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER").inClass("SecurityBypasserUsage$").build()
        );
    }

}
