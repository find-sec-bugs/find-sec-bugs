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
package com.h3xstream.findsecbugs.password;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.testng.annotations.Test;

public class ConstantPasswordDetectorTest extends BaseDetectorTest {
    @Test
    public void detectHardCodePasswordsAndKeys() throws Exception {
        String[] files = {
                getClassFilePath("testcode/password/ConstantPasswords"),
                getClassFilePath("testcode/oauth/SpringServerConfig"),
                getClassFilePath("testcode/oauth/VertxOauth2Config")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        List<Integer> linesPasswords = Arrays.asList(
                44, 52, 57, 62, 67, 72, 80, 86, 87, 88, 89, 91, 92, 93, 94,
                121, 123, 159, 171, 181
        );
        List<Integer> linesKeys = Arrays.asList(
                100, 101, 102, 104, 105, 106, 107, 108, 109, 110, 116, 129,
                130, 131, 133, 134, 135, 136, 137, 138, 144, 150
        );
        for (Integer line : linesPasswords) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("ConstantPasswords").atLine(line)
                            .build()
            );
        }
        for (Integer line : linesKeys) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_KEY")
                            .inClass("ConstantPasswords").atLine(line)
                            .build()
            );
        }

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("HARD_CODE_PASSWORD")
                        .inClass("SpringServerConfig").atLine(22)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("HARD_CODE_PASSWORD")
                        .inClass("VertxOauth2Config").atLine(13)
                        .build()
        );

        // +2 for OAuth and +1 for hard coded public key field
        verify(reporter, times(linesPasswords.size() + 2)).doReportBug(
                bugDefinition().bugType("HARD_CODE_PASSWORD").build());
        verify(reporter, times(linesKeys.size() + 1)).doReportBug(
                bugDefinition().bugType("HARD_CODE_KEY").build());
    }
}
