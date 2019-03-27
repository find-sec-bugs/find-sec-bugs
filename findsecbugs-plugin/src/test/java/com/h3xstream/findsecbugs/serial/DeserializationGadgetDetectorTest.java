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

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DeserializationGadgetDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommonsCollectionGadget() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/commonscollections4/InvokerTransformer")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("DESERIALIZATION_GADGET") //
                        .inClass("InvokerTransformer") //
                        .withPriority("Low") //
                        .build());
    }

    @Test
    public void detectGroovyGadget() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/groovy/ConvertedClosure"),
                getClassFilePath("testcode/serial/groovy/ConversionHandler")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("DESERIALIZATION_GADGET") //
                        .inClass("ConvertedClosure") //
                        .withPriority("Low") //
                        .build());
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("DESERIALIZATION_GADGET") //
                        .inClass("ConversionHandler") //
                        .withPriority("Medium") //
                        .build());
    }

    @Test
    public void detectSpringGadget() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/serial/spring/MethodInvokeTypeProvider"),
                getClassFilePath("testcode/serial/spring/TypeProvider")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("DESERIALIZATION_GADGET") //
                        .inClass("MethodInvokeTypeProvider") //
                        .withPriority("Medium") //
                        .build());
    }


    @Test
    public void detectCommonsCollectionAndSpringGadgetInKotlin() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/TypeProvider"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/InvokerTransformer"),
                getClassFilePath("com/h3xstream/findsecbugs/deserialisation/MethodInvokeTypeProvider")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("DESERIALIZATION_GADGET") //
                        .inClass("MethodInvokeTypeProvider") //
                        .withPriority("Medium") //
                        .build());

        //Assertions
        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("DESERIALIZATION_GADGET") //
                        .inClass("InvokerTransformer") //
                        .withPriority("Low") //
                        .build());
    }
}
