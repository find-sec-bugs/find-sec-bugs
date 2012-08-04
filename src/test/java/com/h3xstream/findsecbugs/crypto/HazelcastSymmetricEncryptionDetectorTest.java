package com.h3xstream.findsecbugs.crypto;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.testng.annotations.Test;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;

public class HazelcastSymmetricEncryptionDetectorTest extends BaseDetectorTest {


    @Test
    public void detectHazelcastSymmetric() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/HazelcastSymmetric")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

	    //Identify the constructor
	    verify( reporter, times(1) ).doReportBug(
                bugDefinition()
                        .bugType( "HAZELCAST_SYMMETRIC_ENCRYPTION" )
                        .inClass( "HazelcastSymmetric" )
                        .inMethod( "init" )
                        .atLine( 26 )
                .build()
        );
    }

}
