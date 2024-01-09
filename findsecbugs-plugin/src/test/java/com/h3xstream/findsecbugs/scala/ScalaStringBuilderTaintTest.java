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

import static org.mockito.Mockito.*;

public class ScalaStringBuilderTaintTest extends BaseDetectorTest {

    /**
     * Test the taint propagation for Scala StringBuilder
     * @throws Exception
     */
    @Test
    public void detectTaintedStringBuilder() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_stringbuilder_taint.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("PATH_TRAVERSAL_IN")
                        .inClass("ScalaStringBuilderTaint").inMethod("unsafeFileFromCharAppend")
                        .build()
        );
    }
}
