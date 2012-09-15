package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UnencryptedSocketDetectorTest extends BaseDetectorTest {

    @Test
    public void detectUnencryptedSocket() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/UnencryptedSocket")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType( "UNENCRYPTED_SOCKET" )
                        .inClass( "UnencryptedSocket" )
                        .inMethod( "plainSocket" )
                        .atLine(26)
                        .build()
        );

        for (Integer line : Arrays.asList(31, 34, 37)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType( "UNENCRYPTED_SOCKET" )
                            .inClass( "UnencryptedSocket" )
                            .inMethod( "otherConstructors" )
                            .atLine(line)
                            .build()
            );
        }
    }

}
