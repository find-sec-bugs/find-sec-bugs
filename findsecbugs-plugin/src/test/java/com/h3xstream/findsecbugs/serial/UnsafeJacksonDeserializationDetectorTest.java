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
package com.h3xstream.findsecbugs.serial;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class UnsafeJacksonDeserializationDetectorTest extends BaseDetectorTest {

    private String BUG_TYPE = "JACKSON_UNSAFE_DESERIALIZATION";

    @Test
    public void avoidJacksonFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/JacksonSerialisationFalsePositive")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .build()
        );
    }

    @Test
    public void avoidJacksonFalsePositiveInKotlin() throws Exception {
        String className = "JacksonSerialisationFalsePositive";

        //Locate test code
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/jackson/JacksonSerialisationFalsePositive"),
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className)
                        .build()
        );

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className + "$Bean")
                        .build()
        );
    }

    @Test
    public void testJacksonDeserialization() throws Exception {
        final String className = "UnsafeJacksonObjectDeserialization";
        final String classPath = "testcode/serial/" + className;
        //Locate test code
        String[] files = {
                getClassFilePath(classPath),
                getClassFilePath(classPath + "$AnotherBean"),
                getClassFilePath(classPath + "$YetAnotherBean"),
        };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className + "$AnotherBean")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className + "$YetAnotherBean")
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className).inMethod("exampleOne").atLine(25)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className).inMethod("exampleTwo")
                        .build()
        );

    }

    @Test
    public void testJacksonDeserializationKotlin() throws Exception {
        String className = "UnsafeJacksonObjectDeserialization";

        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/jackson/UnsafeJacksonObjectDeserialization"),
                getClassFilePath("com/h3xstream/findsecbugs/jackson/UnsafeJacksonObjectDeserialization$AnotherBean"),
                getClassFilePath("com/h3xstream/findsecbugs/jackson/UnsafeJacksonObjectDeserialization$YetAnotherBean"),
        };

        // Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className + "$AnotherBean").atField("obj")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className + "$YetAnotherBean").atField("obj")
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className).inMethod("exampleOne").atLine(26)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType(BUG_TYPE)
                        .inClass(className).inMethod("exampleTwo").atLine(33)
                        .build()
        );

    }

}
