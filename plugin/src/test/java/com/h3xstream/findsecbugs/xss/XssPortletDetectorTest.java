package com.h3xstream.findsecbugs.xss;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class XssPortletDetectorTest extends BaseDetectorTest {


    @Test
    public void detectPortlet() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xss/portlets/XssPortlet")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_SERVLET")
                        .inClass("XssPortlet")
                        .atLine(24)
                        .build()
        );
    }


    @Test
    public void detectIbmPortlet() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xss/portlets/XssIbmPortlet")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XSS_SERVLET")
                        .inClass("XssIbmPortlet")
                        .atLine(25)
                        .build()
        );
    }

}
