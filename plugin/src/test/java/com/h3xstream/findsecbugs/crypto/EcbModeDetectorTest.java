package com.h3xstream.findsecbugs.crypto;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class EcbModeDetectorTest extends BaseDetectorTest {


    @Test
    public void detectEcbMode() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/BlockCipherList")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

	    for (Integer line : Arrays.asList( 18, 19 )) {
		    verify(reporter).doReportBug(
				    bugDefinition()
						    .bugType( "ECB_MODE" )
						    .inClass( "BlockCipherList" )
						    .inMethod( "main" )
						    .atLine( line )
					.build()
		    );
	    }

	    //The count make sure no other bug are detect
	    verify(reporter, times(2)).doReportBug(
			    bugDefinition()
					    .bugType( "ECB_MODE" )
					    .inClass( "BlockCipherList" )
					    .inMethod( "main" )
				.build() );
    }

}
