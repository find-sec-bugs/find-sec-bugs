package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SpelInjectionTest extends BaseDetectorTest {
    @Test
    public void detectInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/script/SpelSample")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for(Integer line : Arrays.asList(31, 47, 63)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SPEL_INJECTION")
                            .inClass("SpelSample")
                            .atLine(line)
                            .build()
            );
        }

        //Out of the 6 calls, 3 are suspicious
        verify(reporter,times(3)).doReportBug(
                bugDefinition()
                        .bugType("SPEL_INJECTION")
                        .inClass("SpelSample")
                        .build()
        );
    }
}
