package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class MessageDigestDetectorTest extends BaseDetectorTest {

    @Test
    public void detectWeakDigest() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/WeakMessageDigest")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

    }
}
