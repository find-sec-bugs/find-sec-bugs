package com.h3xstream.findsecbugs.endpoint;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JaxWsEndpointDetectorTest extends BaseDetectorTest {

    @Test
    public void detectJaxRsEndpoint() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/JaxWsService")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JAXWS_ENDPOINT")
                        .inClass("JaxWsService").inMethod("ping")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JAXWS_ENDPOINT")
                        .inClass("JaxWsService").inMethod("hello")
                        .build()
        );
    }
}
