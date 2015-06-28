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
                getClassFilePath("testcode/password/ConstantPasswords")
        };

        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        List<Integer> lines = Arrays.asList(
                44, 52, 57, 62, 67, 72, 80, 86, 87, 88, 89, 91, 92, 93, 94, 100,
                101, 102, 104, 105, 106, 107, 108, 109, 110, 116, 121, 123, 129,
                130, 131, 133, 134, 135, 136, 137, 138, 144, 150, 36, 37, 39,
                159, 171
        );
        for (Integer line : lines) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("ConstantPasswords").atLine(line)
                            .build()
            );
        }

        verify(reporter, times(lines.size())).doReportBug(
                bugDefinition().bugType("HARD_CODE_PASSWORD").build());
    }
}
