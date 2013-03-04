package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class InsufficientKeySizeBlowfishDetectorTest extends BaseDetectorTest {

    @Test
    public void detectBadKeySize() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/InsufficientKeySizeBlowfish")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(12,20)) {
            verify(reporter).doReportBug(
                    bugDefinition().bugType("BLOWFISH_KEY_SIZE")
                            .inClass("InsufficientKeySizeBlowfish").atLine(line)
                            .build()
            );
        }

        //More than two means a false positive was trigger
        verify(reporter, times(2)).doReportBug(
                bugDefinition().bugType("BLOWFISH_KEY_SIZE").build());
    }
}
