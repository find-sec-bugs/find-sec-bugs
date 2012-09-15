package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ReDosDetectorTest extends BaseDetectorTest {

    @Test
    public void detectRedos() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/VariousRedos")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Field with a Pattern initialize on instantiation
//        verify(reporter).doReportBug(
//                bugDefinition()
//                        .bugType("REDOS")
//                        .inClass("VariousRedos").inMethod("<init>").atLine(8)
//                        .build()
//        );

        //Pattern build in methods
//        for (Integer line : Arrays.asList(16, 26)) {
//            verify(reporter).doReportBug(
//                    bugDefinition()
//                            .bugType("REDOS")
//                            .inClass("VariousRedos").inMethod("main").atLine(line)
//                            .build()
//            );
//        }
    }

}
