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

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class SpringMvcEndpointDetectorTest extends BaseDetectorTest {

    @Test
    public void detectSpringController() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/endpoint/SpringTestController")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        //Assertions
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("hello1").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("hello2").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("hello3").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("hello4").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("hello5").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("hello6").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("helloGet").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("helloPost").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("helloPut").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("helloDelete").build()
        );
        verify(reporter).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").inMethod("helloPatch").build()
        );

        verify(reporter, times(11)).doReportBug(
                bugDefinition().bugType("SPRING_ENDPOINT").build()
        );
    }
}
