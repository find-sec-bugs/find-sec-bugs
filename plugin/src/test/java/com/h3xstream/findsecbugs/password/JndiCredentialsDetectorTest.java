package com.h3xstream.findsecbugs.password;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JndiCredentialsDetectorTest extends BaseDetectorTest {

    @Test
    public void detectHardCodeCredentials() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/password/JndiProperties")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(10,15)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("HARD_CODE_PASSWORD")
                            .inClass("JndiProperties").atLine(line)
                            .build()
            );
        }

        //More than two occurrence == false positive
        verify(reporter,times(2)).doReportBug(
                bugDefinition().bugType("HARD_CODE_PASSWORD").build());
    }
}
