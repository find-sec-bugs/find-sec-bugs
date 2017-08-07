package com.h3xstream.findsecbugs.common;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class ByteCodeTraceTest extends BaseDetectorTest {


    @Test
    public void avoidFalsePositive() throws Exception {
        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true); //Print all instructions

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/command/CommandInjectionSafe"),
                getClassFilePath("bytecode_samples/scala_command_injection.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(false);

    }

}
