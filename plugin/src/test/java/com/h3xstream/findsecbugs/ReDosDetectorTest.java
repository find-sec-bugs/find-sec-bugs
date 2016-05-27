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
package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import org.mockito.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ReDosDetectorTest extends BaseDetectorTest {

    //Reusable matcher for REDOS bug

    @Test
    public void analyseSafePattern() {
        BugReporter reporter = mock(BugReporter.class);
        ReDosDetector detector = new ReDosDetector(reporter);

        detector.analyseRegexString("");
        detector.analyseRegexString("[a-zA-Z]+[0-9]*");
        detector.analyseRegexString("(id-[0-9]+)-([0-9A-F]*)");

        verify(reporter, never()).reportBug(bugDefinition().bugType("REDOS").build());
    }

    @Test
    public void analyseSuspectPattern() {

        BugReporter reporter = mock(BugReporter.class);
        ReDosDetector detector = new ReDosDetector(reporter);


        detector.analyseRegexString("((a)+)+");
        verify(reporter).reportBug(bugDefinition().bugType("REDOS").build());

        reset(reporter);

        detector.analyseRegexString("([b-d])(([a]*))+(0-9)");
        verify(reporter).reportBug(bugDefinition().bugType("REDOS").build());
    }

    /**
     * Test that utilize compile code.
     *
     * @throws Exception
     */
    @Test
    public void detectRedosInCode() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/VariousRedos")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Field with a Pattern initialize on instantiation

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("REDOS")
                        .inClass("VariousRedos").atLine(8)
                        .build()
        );

        //Pattern build in methods
        for (Integer line : Arrays.asList(16, 26)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("REDOS")
                            .inClass("VariousRedos").inMethod("main").atLine(line)
                            .build()
            );
        }
    }
}
