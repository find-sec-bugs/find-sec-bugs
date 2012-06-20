package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class ReDosDetectorTest extends BaseDetectorTest {

    @Test
    public void detectRedos() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/VariousRedos")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);
    }

}
