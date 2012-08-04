package com.h3xstream.findsecbugs.crypto;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.testng.annotations.Test;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;

public class EcbModeDetectorTest extends BaseDetectorTest {


    @Test
    public void detectEcbMode() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/EcbMode")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

	    for (Integer line : Arrays.asList( 15, 16, 19, 20, 23, 24, 25, 26, 27 )) {
		    verify( reporter ).doReportBug(
				    bugDefinition()
						    .bugType( "ECB_MODE" )
						    .inClass( "EcbMode" )
						    .inMethod( "main" )
						    .atLine( line )
					.build()
		    );
	    }

	    //The count make sure no other bug are detect
	    verify( reporter, times( 9 )).doReportBug(
			    bugDefinition()
					    .bugType( "ECB_MODE" )
					    .inClass( "EcbMode" )
					    .inMethod( "main" )
				.build() );
    }

}
