package com.h3xstream.findsecbugs.crypto;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.testng.annotations.Test;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;

public class BadHexadecimalConversionDetectorTest extends BaseDetectorTest {


    @Test
    public void detectBadHexa() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/crypto/BadHexa")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

	    verify( reporter ).doReportBug(
			    bugDefinition()
					    .bugType( "BAD_HEXA_CONVERSION" )
					    .inClass( "BadHexa" )
					    .inMethod( "badHash" )
				.build()
	    );

	    verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType( "BAD_HEXA_CONVERSION" )
                        .inClass( "BadHexa" )
		                .inMethod( "goodHash" )
                .build()
        );
    }
}
