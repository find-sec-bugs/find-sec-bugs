package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JaxRsEndpointDetectorTest extends BaseDetectorTest {

    @Test
    public void detectJaxRsEndpoint() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/JaxRsService")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JAXRS_ENDPOINT")
                        .inClass("JaxRsService").inMethod("hello")
                        .build()
        );
    }
}
