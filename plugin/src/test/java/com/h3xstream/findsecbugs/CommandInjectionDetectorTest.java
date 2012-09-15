package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CommandInjectionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommandInjection() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/CommandInjection")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(18, 20, 24, 28)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjection").inMethod("main").atLine(line)
                            .build()
            );
        }
    }
}
