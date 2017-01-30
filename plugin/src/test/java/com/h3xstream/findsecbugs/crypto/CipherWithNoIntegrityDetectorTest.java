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
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testng.annotations.Test;

public class CipherWithNoIntegrityDetectorTest extends BaseDetectorTest {

    /**
     * General tests plus test to confirm the fix of
     * https://github.com/h3xstream/find-sec-bugs/issues/24
     *
     * @throws Exception
     */
    @Test
    public void detectNoIntegrity() throws Exception {
        //Locate test code
        String[] files = {
            getClassFilePath("testcode/crypto/CipherNoIntegrity"),
            getClassFilePath("testcode/crypto/CipherNoIntegrityBugFixRsa")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        List<Integer> linesECB = Arrays.asList(9, 11);
        for (Integer line : linesECB) {
            verify(reporter).doReportBug(
                    bugDefinition()
                    .bugType("ECB_MODE")
                    .inClass("CipherNoIntegrity").atLine(line)
                    .build()
            );
        }
        verify(reporter, times(linesECB.size())).doReportBug(
                bugDefinition()
                .bugType("ECB_MODE")
                .build()
        );

        List<Integer> linesNoIntegrity = Arrays.asList(9, 10, 11, 12, 21);
        for (Integer line : linesNoIntegrity) {
            verify(reporter).doReportBug(
                    bugDefinition()
                    .bugType("CIPHER_INTEGRITY")
                    .inClass("CipherNoIntegrity").atLine(line)
                    .build()
            );
        }
        verify(reporter, times(linesNoIntegrity.size())).doReportBug(
                bugDefinition()
                .bugType("CIPHER_INTEGRITY")
                .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                .bugType("PADDING_ORACLE")
                .inClass("CipherNoIntegrity").atLine(12)
                .build()
        );
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                .bugType("PADDING_ORACLE")
                .build()
        );
    }
}
