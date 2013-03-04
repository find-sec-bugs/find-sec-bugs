package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InsufficientKeySizeRsaDetectorTest extends BaseDetectorTest {
    @Test
    public void detectBadKeySize() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/InsufficientKeySizeRsa")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(15, 23)) {
            verify(reporter).doReportBug(
                    bugDefinition().bugType("RSA_KEY_SIZE")
                            .inClass("InsufficientKeySizeRsa").atLine(line)
                            .build()
            );
        }

        //More than two means a false positive was trigger
        verify(reporter, times(2)).doReportBug(
                bugDefinition().bugType("RSA_KEY_SIZE").build());
    }
}
