package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JstlOutDetectorTest extends BaseDetectorTest {

    @Test
    public void jspEscape1_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_escape_1.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,never()).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }

    @Test
    public void jspEscape2_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_escape_2.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Only one
        verify(reporter,never()).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }

    @Test
    public void jspEscape3_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("jstl/jstl_escape_3.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);


        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_JSTL_OUT")
                        .inJspFile("jstl/jstl_escape_3.jsp")
                        .atJspLine(3)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_JSTL_OUT").build());
    }
}
