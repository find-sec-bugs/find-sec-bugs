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
package com.h3xstream.findsecbugs.xss;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.*;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Before running theses tests cases, jsp files need to be compiled.
 *
 * <pre>mvn clean test-compile</pre>
 */
public class JspXssDetectorTest extends BaseDetectorTest {

    @BeforeMethod
    public void beforeTest() {
        FindSecBugsGlobalConfig.getInstance().setReportPotentialXssWrongContext(true);
    }

    @AfterMethod
    public void afterTest() {
        FindSecBugsGlobalConfig.getInstance().setReportPotentialXssWrongContext(false);
    }

    @Test
    public void detectXssDirectUse() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_1_direct_use.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(10, 15)) { //Generated classes lines doesn't match original JSP
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("XSS_JSP_PRINT")
                            .inJspFile("xss/xss_1_direct_use.jsp")
                            .atJspLine(line)
                            .build()
            );
        }
        
        for (Integer line : Arrays.asList(12, 17)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("XSS_JSP_PRINT")
                            .inJspFile("xss/xss_1_direct_use.jsp")
                            .atJspLine(line)
                            .withPriority("Low")
                            .build()
            );
        }

        verify(reporter, times(4)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssTransferLocal() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_2_transfer_local.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        //Generated classes lines doesn't match original JSP
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("xss/xss_2_transfer_local.jsp")
                        .atJspLine(10)
                        .build()
        );
        
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("xss/xss_2_transfer_local.jsp")
                        .atJspLine(14)
                        .withPriority("Low")
                        .build()
        );

        verify(reporter, times(2)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssFalsePositiveSafeInput() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_3_false_positive_static_function.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //No alert should be trigger
        verify(reporter, never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssFalsePositiveOverwriteLocal() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_4_false_positive_overwrite_local.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //No alert should be trigger
        verify(reporter, never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssMultipleTransfertLocal() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_5_multiple_transfer_local.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("xss/xss_5_multiple_transfer_local.jsp")
                        .atJspLine(13)
                        .build()
        );
        
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("xss/xss_5_multiple_transfer_local.jsp")
                        .atJspLine(17)
                        .withPriority("Low")
                        .build()
        );

        verify(reporter, times(2)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssGetParameter() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_6_get_parameter.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_JSP_PRINT")
                        .inJspFile("xss/xss_6_get_parameter.jsp")
                        .atJspLine(7)
                        .build()
        );

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssFalsePositiveDirectCast() throws Exception {
        //Locate test code
        String[] files = {
            getJspFilePath("xss/xss_7_false_positive_direct_cast.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //No alert should be trigger
        verify(reporter, never()).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void detectXssRequestAttributeWithCustomConfiguration() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_8_request_attribute.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());

        String customConfigFile = FindSecBugsGlobalConfig.getInstance().getCustomConfigFile();
        String path = this.getClass().getResource("/com/h3xstream/findsecbugs/xss/CustomConfig.txt").getPath();
        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(path);

        try {
            analyze(files, reporter);
        } finally {
            FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(customConfigFile == null ? "" : customConfigFile);
        }

        for (Integer line : Arrays.asList(16, 17)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("XSS_JSP_PRINT")
                            .inJspFile("xss/xss_8_request_attribute.jsp")
                            .atJspLine(line)
                            .build()
            );
        }

        verify(reporter, times(2)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }

    @Test
    public void owaspTags() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("xss/xss_9_owasp_taglib.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(7)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("XSS_JSP_PRINT")
                            .inJspFile("xss/xss_9_owasp_taglib.jsp")
                            .atJspLine(line)
                            .build()
            );
        }

        verify(reporter, times(1)).doReportBug(bugDefinition().bugType("XSS_JSP_PRINT").build());
    }
}

