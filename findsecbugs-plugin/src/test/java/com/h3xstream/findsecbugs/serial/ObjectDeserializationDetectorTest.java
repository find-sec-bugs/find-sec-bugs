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
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by aminozhenko on 11/11/2015.
 */
public class ObjectDeserializationDetectorTest extends BaseDetectorTest {


    @Test
    public void detectObjectDeserialization() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/ObjectDeserialization")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObject").atLine(14)
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObject")
                        .build()
        );
    }

    @Test
    public void detectObjectDeserializationInKotlin() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/ObjectDeserialization"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/SerialisationFalsePositive"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/UserEntity")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObject").atLine(13)
                        .build()
        );

        verify(reporter, times(1)).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObject")
                        .build()
        );
    }

    @Test
    public void detectObjectDeserializationClassLoaderObjectInputStream() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/ObjectDeserialization")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObjectWithInheritance").atLine(24)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObjectWithInheritance")
                        .build()
        );
    }

    @Test
    public void detectObjectDeserializationClassLoaderObjectInputStreamInKotlin() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/ObjectDeserialization"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/SerialisationFalsePositive"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/UserEntity")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObjectWithInheritance").atLine(23)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectDeserialization").inMethod("deserializeObjectWithInheritance")
                        .build()
        );
    }

    @Test
    public void avoidReadObjectFalsePositive() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/ObjectDeserializationFalsePositive1")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .build()
        );
    }

    @Test
    public void avoidReadObjectFalsePositiveInKotlin() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/ObjectDeserialization"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/SerialisationFalsePositive"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/UserEntity")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("SerialisationFalsePositive")
                        .build()
        );
    }

    @Test
    public void avoidReadObjectFalsePositiveASN1() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/ObjectDeserializationFalsePositive2")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .build()
        );
    }


    @Test
    public void detectObjectInputSignature() throws Exception {
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/ObjectInputSig")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectInputSig").inMethod("deserialize1")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .inClass("ObjectInputSig").inMethod("deserialize2")
                        .build()
        );

        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("OBJECT_DESERIALIZATION")
                        .build()
        );
    }



}
