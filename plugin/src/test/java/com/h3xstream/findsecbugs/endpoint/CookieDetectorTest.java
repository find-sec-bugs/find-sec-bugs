package com.h3xstream.findsecbugs.endpoint;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CookieDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCookieUsage() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/CookieUsage")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(15,16,17)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COOKIE_USAGE")
                            .inClass("CookieUsage").inMethod("doGet").atLine(15)
                            .build()
            );
        }
    }
}
