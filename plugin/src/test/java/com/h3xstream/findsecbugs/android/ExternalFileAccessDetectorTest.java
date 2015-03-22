package com.h3xstream.findsecbugs.android;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ExternalFileAccessDetectorTest extends BaseDetectorTest {


    @Test
    public void detectExternalFileAccess() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/android/ExternalFileAccess")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        int line = 19; //First line
        while(line++ <= 23) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("EXTERNAL_FILE_ACCESS")
                            .inClass("ExternalFileAccess")
                            .inMethod("onCreate")
                            .atLine(line)
                            .build()
            );
        }

        //The count make sure no other bug are detect
        verify(reporter, times(5)).doReportBug(
                bugDefinition()
                        .bugType("EXTERNAL_FILE_ACCESS")
                        .inClass("ExternalFileAccess")
                        .inMethod("onCreate")
                        .build());
    }

}
