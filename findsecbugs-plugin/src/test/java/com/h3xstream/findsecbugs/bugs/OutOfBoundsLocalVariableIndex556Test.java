package com.h3xstream.findsecbugs.bugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class OutOfBoundsLocalVariableIndex556Test extends BaseDetectorTest {

    @Test
    public void testForRegression575() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/bugs/OutOfBoundsLocalVariableIndex556.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

    }

}
