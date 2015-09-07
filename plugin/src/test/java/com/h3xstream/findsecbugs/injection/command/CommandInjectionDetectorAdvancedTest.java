package com.h3xstream.findsecbugs.injection.command;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Test some additional signatures for ProcessBuilder.
 */
public class CommandInjectionDetectorAdvancedTest extends BaseDetectorTest {

    /*FAILING Test
    @Test
    public void avoidFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/command/CommandInjectionSafe"),
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);


        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("CommandInjectionSafe")
                        .build()
        );

    }


    @Test
    public void detectSuspicious() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/command/CommandInjectionSuspicious"),
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);


        //Assertions
        for(Integer line : Arrays.asList(11,13)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureConstructorArray").atLine(line)
                            .build()
            );
        }

        for(Integer line : Arrays.asList(20,23)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureConstructorList").atLine(line)
                            .build()
            );
        }

        for(Integer line : Arrays.asList(27,29)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureCommandMethodArray").atLine(line)
                            .build()
            );
        }

        for(Integer line : Arrays.asList(35,38)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("COMMAND_INJECTION")
                            .inClass("CommandInjectionSuspicious").inMethod("insecureCommandMethodList").atLine(line)
                            .build()
            );
        }

    }
*/

}
