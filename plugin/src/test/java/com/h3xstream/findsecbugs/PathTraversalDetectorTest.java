package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PathTraversalDetectorTest extends BaseDetectorTest {

    @Test
    public void detectPathTraversal() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/PathTraversal")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(10, 11, 13, 15, 17)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("PATH_TRAVERSAL_IN")
                            .inClass("PathTraversal").inMethod("main").atLine(line)
                            .build()
            );
        }

        for (Integer line : Arrays.asList(20, 21, 23, 24)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("PATH_TRAVERSAL_OUT")
                            .inClass("PathTraversal").inMethod("main").atLine(line)
                            .build()
            );
        }
    }
}
