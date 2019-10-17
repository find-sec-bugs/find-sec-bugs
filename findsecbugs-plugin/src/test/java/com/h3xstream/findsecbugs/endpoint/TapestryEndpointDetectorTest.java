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
package com.h3xstream.findsecbugs.endpoint;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TapestryEndpointDetectorTest extends BaseDetectorTest {

    /**
     * This first code sample has some Annotation and Type imports.
     *
     * @throws Exception
     */
    @Test
    public void detectTapestryPageWithFrameworkReference() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/pages/TapestryPage")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("TAPESTRY_ENDPOINT")
                        .inClass("TapestryPage")
                        .build()
        );
    }

    /**
     * This code sample is a plain POJO. The package name will trigger the pointer.
     *
     * @throws Exception
     */
    @Test
    public void detectTapestryPagePojo() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/pages/Index")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("TAPESTRY_ENDPOINT")
                        .inClass("Index")
                        .build()
        );
    }

    /**
     * Make sure that class not in a package containing ".pages"
     * as not mark as endpoint.
     */
    @Test
    public void noFalsePositiveOnOtherClass() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/endpoint/JaxRsService"),
                getClassFilePath("testcode/endpoint/JaxWsService")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("TAPESTRY_ENDPOINT")
                        .build()
        );
    }
}
