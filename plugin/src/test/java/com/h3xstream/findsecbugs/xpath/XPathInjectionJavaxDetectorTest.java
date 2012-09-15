package com.h3xstream.findsecbugs.xpath;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class XPathInjectionJavaxDetectorTest extends BaseDetectorTest {

    @Test
    public void detectXPathInjectionJavax() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathJavax")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(21, 29)) {
            verify(reporter).doReportBug(
                    bugDefinition().bugType("XPATH_INJECTION")
                            .inClass("XPathJavax").inMethod("main").atLine(line)
                            .build()
            );
        }

        //More than two means a false positive was trigger
        verify(reporter, times(2)).doReportBug(
                bugDefinition().bugType("XPATH_INJECTION").build());
    }
}
