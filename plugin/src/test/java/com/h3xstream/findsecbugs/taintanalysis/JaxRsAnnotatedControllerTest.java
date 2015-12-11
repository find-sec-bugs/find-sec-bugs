package com.h3xstream.findsecbugs.taintanalysis;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class JaxRsAnnotatedControllerTest extends BaseDetectorTest {

        @Test
        public void testSafeTestCases() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/taint/JaxRsAnnotatedController")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE") //
                        .inClass("JaxRsAnnotatedController").inMethod("getInfoUser") //
                        .withPriority("High") //High because of taint parameter
                        .build());

        verify(reporter,times(1)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE").build());
    }
}
