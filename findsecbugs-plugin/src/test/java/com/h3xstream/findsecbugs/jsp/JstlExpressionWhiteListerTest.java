package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;
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
import static org.mockito.Mockito.*;


public class JstlExpressionWhiteListerTest extends BaseDetectorTest {

    @Test
    public void jstlExpressionSecure() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_expression_secure.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }


    @Test
    public void jstlExpressionInsecure() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_expression_insecure.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,times(3)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }


}
