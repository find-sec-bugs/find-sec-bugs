package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import edu.umd.cs.findbugs.SystemProperties;
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

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.*;


public class JstlExpressionWhiteListerTest extends BaseDetectorTest {

    @Test
    public void jstlExpression1_Secure() throws Exception {
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
    public void jstlExpression2_Insecure() throws Exception {
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

    @Test
    public void jstlExpression3_CustomSecure() throws Exception {
        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/jsp/CustomWhiteList.txt");
        File configFile = new File(configUrl.toURI());

        SystemProperties.setProperty("findsecbugs.jstlsafe.customregexfile", configFile.getCanonicalPath());

        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_expression_custom_secure.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());

        //Avoid potential conflicts with other tests.
        //There will still remain a custom sink load if the test suite does not reload all classes. (Minor artifact)
        SystemProperties.setProperty("findsecbugs.jstlsafe.customregexfile", "");
    }

    @Test
    public void jstlExpression4_CustomInsecure() throws Exception {
        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/jsp/CustomWhiteList.txt");
        File configFile = new File(configUrl.toURI());

        SystemProperties.setProperty("findsecbugs.jstlsafe.customregexfile", configFile.getCanonicalPath());

        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_expression_custom_insecure.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,times(3)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());

        //Avoid potential conflicts with other tests.
        //There will still remain a custom sink load if the test suite does not reload all classes. (Minor artifact)
        SystemProperties.setProperty("findsecbugs.jstlsafe.customregexfile", "");
    }
}
