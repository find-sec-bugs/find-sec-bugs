package com.h3xstream.findsecbugs;

import static org.mockito.Mockito.*;

import org.testng.annotations.Test;

import edu.umd.cs.findbugs.BaseDetectorTest;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.EasyBugReporter;

import java.net.URL;

public class PredictableRandomDetectorTest extends BaseDetectorTest {

	
	@Test
	public void detectUsePredictableRandom() throws Exception {
        //Locate test code
		String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/testcode/InsecureRandom")
        };

        //Run the analysis
		BugReporter reporter = spy(new EasyBugReporter());
		analyze(files, reporter);

        //TODO: Do assertions on the bug report
		//expect(reporter.reportBug(eq()))
		
	}
	
}
