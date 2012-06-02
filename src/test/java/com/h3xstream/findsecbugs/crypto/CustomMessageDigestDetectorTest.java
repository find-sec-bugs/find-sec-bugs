package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CustomMessageDigestDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCustomDigest() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/CustomMessageDigest")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("CUSTOM_MESSAGE_DIGEST")
                        .inClass("CustomMessageDigest")
                        .build()
        );
    }

}
