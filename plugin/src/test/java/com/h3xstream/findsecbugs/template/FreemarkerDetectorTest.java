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
package com.h3xstream.findsecbugs.template;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FreemarkerDetectorTest extends BaseDetectorTest {

    @Test
    public void basicUsagesTesting() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/template/FreemarkerUsage")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition().bugType("TEMPLATE_INJECTION_FREEMARKER") //
                        .inClass("FreemarkerUsage").inMethod("simple").atLine(42) //
                        .withPriority("Medium") //High because of taint parameter
                        .build());

        verify(reporter,times(1)).doReportBug(
                bugDefinition().bugType("TEMPLATE_INJECTION_FREEMARKER")
                        .inClass("FreemarkerUsage").inMethod("simple") //
                        .build());
    }


    @Test
    public void allSignatures() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/template/FreemarkerUsage")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        //Assertions
        for(Integer line : Arrays.asList(57,58,59)) {
            verify(reporter).doReportBug(
                    bugDefinition().bugType("TEMPLATE_INJECTION_FREEMARKER") //
                            .inClass("FreemarkerUsage").inMethod("allSignatures").atLine(line) //
                            .withPriority("Medium") //High because of taint parameter
                            .build());
        }

        verify(reporter,times(3)).doReportBug(
                bugDefinition().bugType("TEMPLATE_INJECTION_FREEMARKER")
                        .inClass("FreemarkerUsage").inMethod("allSignatures") //
                        .build());
    }

}
