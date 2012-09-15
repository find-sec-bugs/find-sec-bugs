package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RsaNoPaddingDetectorTest extends BaseDetectorTest {

    @Test
    public void detectRsaNoPadding() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/RsaNoPadding")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("RSA_NO_PADDING")
                        .inClass("RsaNoPadding")
                        .inMethod("rsaCipherWeak")
                        .atLine(15)
                        .build()
        );

        verify(reporter,times(1)).doReportBug(
                bugDefinition()
                        .bugType("RSA_NO_PADDING")
                        .build()
        );
    }

}
