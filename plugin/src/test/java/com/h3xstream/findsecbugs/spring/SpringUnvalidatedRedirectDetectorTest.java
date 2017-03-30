package com.h3xstream.findsecbugs.spring;


import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SpringUnvalidatedRedirectDetectorTest extends BaseDetectorTest {

    @Test
    public void detectSpringUnvalidatedRedirect() throws Exception {
        //Locate test code
        String[] files = {
          getClassFilePath("testcode/spring/SpringUnvalidatedRedirectController")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("redirect1")
                        .atLine(12)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("redirect2")
                        .atLine(17)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPRING_UNVALIDATED_REDIRECT")
                        .inClass("SpringUnvalidatedRedirectController")
                        .inMethod("buildRedirect")
                        .atLine(27)
                        .build()
        );
    }
}
