package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class Struts2EndpointDetectorTest extends BaseDetectorTest {

    @Test
    public void detectStruts2Endpoint() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/struts2/StrutsV2Endpoint")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("STRUTS2_ENDPOINT")
                        .inClass("StrutsV2Endpoint")
                        .build()
        );
    }
}
