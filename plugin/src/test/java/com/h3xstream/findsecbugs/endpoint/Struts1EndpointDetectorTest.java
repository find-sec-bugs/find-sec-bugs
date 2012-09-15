package com.h3xstream.findsecbugs.endpoint;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class Struts1EndpointDetectorTest extends BaseDetectorTest {

    @Test
    public void detectStruts1Endpoint() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/struts1/StrutsV1Action"),
                getClassFilePath("testcode/struts1/TestForm")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("STRUTS1_ENDPOINT")
                        .inClass("StrutsV1Action")
                        .build()
        );
    }
}
