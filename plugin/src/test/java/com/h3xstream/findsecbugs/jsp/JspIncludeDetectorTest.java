package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class JspIncludeDetectorTest extends BaseDetectorTest {

    @Test
    public void jspInclude1_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("includes/jsp_include_1.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_INCLUDE")
                        .inJspFile("includes/jsp_include_1.jsp")
                        .atJspLine(4)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }

    @Test
    public void jspInclude2_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("includes/jsp_include_2_safe.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, never()).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }


    @Test
    public void jspInclude3_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("includes/jsp_include_3.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_INCLUDE")
                        .inJspFile("includes/jsp_include_3.jsp")
                        .atJspLine(8)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_INCLUDE").build());
    }
}
