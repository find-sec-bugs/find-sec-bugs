package com.h3xstream.findsecbugs.xpath;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class XPathInjectionApacheXPathApiDetectorTest extends BaseDetectorTest {
    @Test
    public void detectXPathInjectionJavax() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathApacheXPathApi")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);


    }
}
