package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DesUsageDetectorTest extends BaseDetectorTest {

    @Test
    public void detectDes() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/BlockCipherList")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        for (Integer line : Arrays.asList(20, 21, 22, 23, 24, 25, 26, 27)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType( "DES_USAGE" )
                            .inClass( "BlockCipherList" )
                            .inMethod( "main" )
                            .atLine( line )
                            .build()
            );
        }
    }
}
