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
package com.h3xstream.findsecbugs.groovy;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class GroovyShellDetectorTest extends BaseDetectorTest {

    @Test
    public void detectGroovyShellUsageEvaluateMethod() throws Exception {
        String[] files = {
                getClassFilePath("testcode/groovy/GroovyShellUsage.java")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(19,20,21,22,23,24,25,26)) {

            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("GROOVY_SHELL")
                            .inClass("GroovyShellUsage").inMethod("eval").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(8)).doReportBug(
                bugDefinition()
                        .bugType("GROOVY_SHELL")
                        .inClass("GroovyShellUsage").inMethod("eval")
                        .build()
        );
    }

    @Test
    public void detectGroovyShellUsageParseMethod() throws Exception {
        String[] files = {
                getClassFilePath("testcode/groovy/GroovyShellUsage.java")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(32,33,34,35,36,37,38)) {

            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("GROOVY_SHELL")
                            .inClass("GroovyShellUsage").inMethod("parse").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(7)).doReportBug(
                bugDefinition()
                        .bugType("GROOVY_SHELL")
                        .inClass("GroovyShellUsage").inMethod("parse")
                        .build()
        );
    }

    @Test
    public void detectGroovyShellUsageParseClass() throws Exception {
        String[] files = {
                getClassFilePath("testcode/groovy/GroovyShellUsage.java")
        };
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(44,45,46,47,48,49,50)) {

            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("GROOVY_SHELL")
                            .inClass("GroovyShellUsage").inMethod("parseClass").atLine(line)
                            .build()
            );
        }

        verify(reporter,times(7)).doReportBug(
                bugDefinition()
                        .bugType("GROOVY_SHELL")
                        .inClass("GroovyShellUsage").inMethod("parseClass")
                        .build()
        );
    }
}
