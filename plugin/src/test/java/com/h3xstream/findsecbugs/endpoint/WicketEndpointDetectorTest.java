package com.h3xstream.findsecbugs.endpoint;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class WicketEndpointDetectorTest extends BaseDetectorTest {

    @Test
    public void detectWicketEndpoint() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/WicketWebPage")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WICKET_ENDPOINT")
                        .inClass("WicketWebPage")
                        .build()
        );
    }
}
