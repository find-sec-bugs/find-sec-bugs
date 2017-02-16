package com.h3xstream.findsecbugs.bugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class OutOfBoundMutableTest extends BaseDetectorTest {

    @Test(enabled = false)
    public void detectSecureFlagCookieBasic() throws Exception {
        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);
        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        //FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/bugs/OutOfBoundMutableSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);
    }
}
