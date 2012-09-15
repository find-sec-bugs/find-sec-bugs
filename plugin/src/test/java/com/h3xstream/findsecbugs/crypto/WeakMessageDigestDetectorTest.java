package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class WeakMessageDigestDetectorTest extends BaseDetectorTest {

    @Test
    public void detectWeakDigest() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/WeakMessageDigest")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //MD2 usage
        verify(reporter, atLeastOnce()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST")
                        .inClass("WeakMessageDigest").inMethod("main").atLine(16)
                        .build()
        );

        //MD5 usage
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_MESSAGE_DIGEST")
                        .inClass("WeakMessageDigest").inMethod("main").atLine(16)
                        .build()
        );
    }
}
