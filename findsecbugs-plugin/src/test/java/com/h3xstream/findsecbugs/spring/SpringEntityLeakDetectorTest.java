/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.spring;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
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
						.bugType("ENTITY_LEAK")
						.inClass("SpringEntityLeakController")
						.inMethod("api1")
						.build()
		);

		verify(reporter).doReportBug(
				bugDefinition()
						.bugType("ENTITY_LEAK")
						.inClass("SpringEntityLeakController")
						.inMethod("api2")
						.build()
		);

		verify(reporter).doReportBug(
				bugDefinition()
						.bugType("ENTITY_LEAK")
						.inClass("SpringEntityLeakController")
						.inMethod("api4")
						.build()
		);

		//Make sure exactly 3 instances are found
		verify(reporter, times(3)).doReportBug(
				bugDefinition()
						.bugType("ENTITY_LEAK")
						.inClass("SpringEntityLeakController")
						.build()
		);
	}


	@Test
	public void detectEntityMassAssignment() throws Exception {
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
						.bugType("ENTITY_MASS_ASSIGNMENT")
						.inClass("SpringEntityLeakController")
						.inMethod("api3")
						.build()
		);

		verify(reporter).doReportBug(
				bugDefinition()
						.bugType("ENTITY_MASS_ASSIGNMENT")
						.inClass("SpringEntityLeakController")
						.inMethod("api5")
						.build()
		);

		//Make sure exactly 2 instances are found
		verify(reporter, times(2)).doReportBug(
				bugDefinition()
						.bugType("ENTITY_MASS_ASSIGNMENT")
						.inClass("SpringEntityLeakController")
						.build()
		);
	}

	/**
	 * The detector should identify entity class also on interface contracts.
	 * @throws Exception
	 */
	@Test
	public void detectEntityBugFix477() throws Exception {
		//Locate test code
		String[] files = {
				getClassFilePath("testcode/spring/SpringEntityLeakControllerInterface"),
				getClassFilePath("testcode/spring/SampleEntity"),
				getClassFilePath("testcode/spring/SampleEntityTwo")
		};

		//Run the analysis
		EasyBugReporter reporter = spy(new SecurityReporter());
		analyze(files, reporter);

		verify(reporter).doReportBug(
				bugDefinition()
						.bugType("ENTITY_LEAK")
						.inClass("SpringEntityLeakControllerInterface")
						.inMethod("api1")
						.build()
		);
	}
}