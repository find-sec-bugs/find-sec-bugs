package com.h3xstream.findsecbugs;

import static org.mockito.Mockito.*;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

public class PredictableRandomDetectorTest extends BaseDetectorTest {

    @Test
    public void detectUsePredictableRandom() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/InsecureRandom")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(2)).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .build()
        );
        //1rst variation new Random()
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .inClass("InsecureRandom").inMethod("newRandomObj").atLine(9)
                        .build()
        );
        //2nd variation Math.random()
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .inClass("InsecureRandom").inMethod("mathRandom").atLine(16)
                        .build()
        );

    }

}
