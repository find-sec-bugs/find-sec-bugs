package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NullCipherDetectorTest  extends BaseDetectorTest {

    @Test
    public void detectNullCipher() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/NullCipherUse")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);


        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType( "NULL_CIPHER" )
                        .inClass( "NullCipherUse" )
                        .inMethod( "main" )
                        .build()
        );
    }

}
