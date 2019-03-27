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
package com.h3xstream.findsecbugs.kotlin;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KotlinHardcodedPasswordEqualsDetectorTest extends BaseDetectorTest {

    @Test
    public void detectHardCodePasswordsWithEquals() throws Exception {
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/password/EqualsPasswordField"),
                getClassFilePath("com/h3xstream/findsecbugs/password/HardcodedPasswordKt")
        };

        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(15, 24, 33)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("EqualsPasswordField").atLine(line)
                            .build()
            );
        }

        for (Integer line : Arrays.asList(84, 93, 102)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("HardcodedPasswordKt").atLine(line)
                            .build()
            );
        }

        verify(reporter, times(6)).doReportBug(bugDefinition().bugType("HARD_CODE_PASSWORD").build());
    }
}
