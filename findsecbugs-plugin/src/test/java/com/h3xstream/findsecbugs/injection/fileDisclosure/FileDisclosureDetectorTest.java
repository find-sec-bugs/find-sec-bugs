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
package com.h3xstream.findsecbugs.injection.fileDisclosure;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;
import java.util.Arrays;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FileDisclosureDetectorTest extends BaseDetectorTest {

    @Test
    public void detectFileDisclosure() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/file/FileDisclosure")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(20, 22, 24, 26, 29)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("STRUTS_FILE_DISCLOSURE")
                            .inClass("FileDisclosure").inMethod("doGet").atLine(line)
                            .build()
            );
        }

        for (Integer line : Arrays.asList(35, 37, 39, 42)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SPRING_FILE_DISCLOSURE")
                            .inClass("FileDisclosure").inMethod("doGet").atLine(line)
                            .build()
            );
        }

        //Out of 6 Struts ActionForward calls, 5 are suspicious
        verify(reporter, times(5)).doReportBug(
                bugDefinition().bugType("STRUTS_FILE_DISCLOSURE").build()
        );

        //Out of 5 Spring ModelAndView calls, 4 are suspicious
        verify(reporter, times(4)).doReportBug(
                bugDefinition().bugType("SPRING_FILE_DISCLOSURE").build()
        );
    }

    @Test
    public void detectFileDisclosureWithRequestDispatcher() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/file/FileDisclosure")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, times(2)).doReportBug(
                bugDefinition().bugType("REQUESTDISPATCHER_FILE_DISCLOSURE").inMethod("doGet2").build()
        );
    }
}
