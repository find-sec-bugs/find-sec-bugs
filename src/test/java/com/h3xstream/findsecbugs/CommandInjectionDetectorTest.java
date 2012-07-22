package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CommandInjectionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommandInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/CommandInjection")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(18, 20, 25, 29)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").inMethod("main").atLine(line)
                            .build()
            );
        }
    }
}
