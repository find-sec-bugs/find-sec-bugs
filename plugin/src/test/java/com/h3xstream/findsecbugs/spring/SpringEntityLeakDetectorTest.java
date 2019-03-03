package com.h3xstream.findsecbugs.spring;


import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SpringEntityLeakDetectorTest extends BaseDetectorTest {

	@Test
	public void detectEntityLeak() throws Exception {
		//Locate test code
		String[] files = {
				getClassFilePath("testcode/spring/SpringEntityLeakController"),
				getClassFilePath("testcode/spring/SampleEntity"),
				getClassFilePath("testcode/spring/SampleEntityTwo")
		};

		//Run the analysis
		EasyBugReporter reporter = spy(new SecurityReporter());
		analyze(files, reporter);

		//Assertions
		verify(reporter).doReportBug(
				bugDefinition()
						.bugType("SPRING_ENTITY_LEAK")
						.inClass("SpringEntityLeakController")
						.inMethod("api1")
						.build()
		);

		verify(reporter).doReportBug(
				bugDefinition()
						.bugType("SPRING_ENTITY_LEAK")
						.inClass("SpringEntityLeakController")
						.inMethod("api2")
						.build()
		);

		verify(reporter).doReportBug(
				bugDefinition()
						.bugType("SPRING_ENTITY_LEAK")
						.inClass("SpringEntityLeakController")
						.inMethod("api3")
						.build()
		);
	}
}