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
package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class JstlOutDetectorWeblogicTest extends BaseDetectorTest {

    /**
     * This test case cover weblogic jsp which will wrap static value.
     * See bytecode for details
     *
     * @throws Exception
     */
    @Test
    public void jspEscape_weblogic() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/jsp_jstl_out_weblogic12.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_JSTL_OUT")
                        .inClass("__test")
                        .atLine(210)
                        .withPriority("Medium")
                        .build()
        );

        //Only one
        verify(reporter,times(1)).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }

}
