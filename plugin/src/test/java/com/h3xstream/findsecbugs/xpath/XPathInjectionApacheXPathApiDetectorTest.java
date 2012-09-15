package com.h3xstream.findsecbugs.xpath;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class XPathInjectionApacheXPathApiDetectorTest extends BaseDetectorTest {
    @Test
    public void detectXPathInjectionJavax() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathApacheXPathApi")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);


    }
}
