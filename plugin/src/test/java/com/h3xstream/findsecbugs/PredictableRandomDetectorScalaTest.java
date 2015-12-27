package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PredictableRandomDetectorScalaTest extends BaseDetectorTest {

    @Test
    public void detectUsePredictableRandom() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_random.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM_SCALA")
                        .inClass("RandomValueController").inMethod("random1").atLine(14)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM_SCALA")
                        .inClass("RandomValueController").inMethod("random2").atLine(20)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM_SCALA")
                        .inClass("RandomValueController").inMethod("random3").atLine(25)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .inClass("RandomValueController").inMethod("random4").atLine(30)
                        .build()
        );

        for(int line : Arrays.asList(37,38,39,44,45,46,47,48,49,50,51,52,53)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("PREDICTABLE_RANDOM_SCALA")
                            .inClass("RandomValueController").inMethod("random5_various").atLine(line)
                            .build()
            );
        }

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM_SCALA")
                        .inClass("RandomValueController").inMethod("generateSecretToken1").atLine(59)
                        .build()
        );


        //Total bugs found (detect false positive)
        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .build()
        );
        verify(reporter, times(17)).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM_SCALA")
                        .build()
        );

    }
}
