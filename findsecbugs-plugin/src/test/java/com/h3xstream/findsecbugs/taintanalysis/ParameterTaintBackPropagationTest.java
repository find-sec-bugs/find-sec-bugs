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
package com.h3xstream.findsecbugs.taintanalysis;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class ParameterTaintBackPropagationTest extends BaseDetectorTest {

    @Test
    public void testBackPropagation() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/taint/ParameterTaintBackPropagation")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());

        String customConfigFile = FindSecBugsGlobalConfig.getInstance().getCustomConfigFile();
        String path = this.getClass().getResource("/com/h3xstream/findsecbugs/taintanalysis/CustomConfig.txt").getPath();
        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(path);
        try {
            analyze(files, reporter);
        }
        finally {
            FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(customConfigFile == null ? "" : customConfigFile);
        }

        verify(reporter, times(2)).doReportBug(
                bugDefinition().bugType("SERVLET_PARAMETER").build());

        verify(reporter, times(1)).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE")
                        .inClass("ParameterTaintBackPropagation").inMethod("taintedUsingPropagatedParameter")
                        .withPriority("High") //High because of taint parameter
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE")
                        .inClass("ParameterTaintBackPropagation").inMethod("safeByParameterBackPropagation")
                        .build());

        verify(reporter, never()).doReportBug(
                bugDefinition().bugType("SQL_INJECTION_HIBERNATE")
                        .inClass("ParameterTaintBackPropagation").inMethod("safeByTagBackPropagation")
                        .build());
    }
}