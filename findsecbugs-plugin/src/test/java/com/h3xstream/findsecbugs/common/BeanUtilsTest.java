package com.h3xstream.findsecbugs.common;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;

public class BeanUtilsTest extends BaseDetectorTest {
    @Test
    public void detectBeanUtils() throws Exception {

        // Locate test code
        String[] files = {
                getClassFilePath("testcode/common/BeanUtils"),

        };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(false);

    }

        private void analyze (String[]files, EasyBugReporter reporter){
        }
    }



