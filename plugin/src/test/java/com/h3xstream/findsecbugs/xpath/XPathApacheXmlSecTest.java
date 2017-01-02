package com.h3xstream.findsecbugs.xpath;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class XPathApacheXmlSecTest extends BaseDetectorTest {

    @Test
    public void detectXPathInjectionApache() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathApacheXmlSec")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);


        //Assertions
        for (Integer line : Arrays.asList(19, 20, 25, 26, 30, 31)) {
            verify(reporter).doReportBug(
                    bugDefinition().bugType("XPATH_INJECTION")
                            .inClass("XPathApacheXmlSec").inMethod("main").atLine(line)
                            .build()
            );
        }

        //More than three means a false positive was trigger
        verify(reporter, times(6)).doReportBug(
                bugDefinition().bugType("XPATH_INJECTION").build());
    }
}
