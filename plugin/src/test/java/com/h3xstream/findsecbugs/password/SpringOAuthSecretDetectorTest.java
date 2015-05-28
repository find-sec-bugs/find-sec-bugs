package com.h3xstream.findsecbugs.password;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;


import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SpringOAuthSecretDetectorTest  extends BaseDetectorTest {

    @Test
    public void detectHardCodeSecretKey() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/oauth/SpringServerConfig")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("HARD_CODE_PASSWORD")
                        .inClass("SpringServerConfig").atLine(22)
                        .build()
        );

        //More than one occurrence == false positive
        verify(reporter,times(1)).doReportBug(
                bugDefinition().bugType("HARD_CODE_PASSWORD").build());
    }
}
