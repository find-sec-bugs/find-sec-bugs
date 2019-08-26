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
package com.h3xstream.findsecbugs.file;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;

public class OverlyPermissiveFilePermissionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectOverlyPermissive() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/file/permissions/JavaNioPosixApi")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("OVERLY_PERMISSIVE_FILE_PERMISSION")
                        .inClass("JavaNioPosixApi")
                        .inMethod("notOk")
                        .build()
        );

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("OVERLY_PERMISSIVE_FILE_PERMISSION")
                        .inClass("JavaNioPosixApi")
                        .inMethod("ok")
                        .build()
        );
    }

    @Test
    public void detectOverlyPermissiveObjectOriented() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/file/permissions/JavaNioPosixApi")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        verify(reporter,times(3)).doReportBug(
                bugDefinition()
                        .bugType("OVERLY_PERMISSIVE_FILE_PERMISSION")
                        .inClass("JavaNioPosixApi")
                        .inMethod("notOk2")
                        .build()
        );
    }




    @Test
    public void detectOverlyPermissiveRuntimeExec() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/file/permissions/CommandExecChmod")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("OVERLY_PERMISSIVE_FILE_PERMISSION")
                        .inClass("CommandExecChmod")
                        .build()
        );
    }
}
