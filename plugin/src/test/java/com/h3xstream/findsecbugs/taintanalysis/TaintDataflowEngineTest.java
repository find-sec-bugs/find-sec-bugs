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
import org.testng.annotations.Test;

import java.io.Writer;

import static org.mockito.Mockito.*;

public class TaintDataflowEngineTest extends BaseDetectorTest {

    @Test
    public void testDerivedConfiguration() throws Exception {

        FindSecBugsGlobalConfig.getInstance().setDebugOutputTaintConfigs(true);

        TaintDataflowEngine.writer = mock(Writer.class);

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/command/CommandInjection"),
                getClassFilePath("testcode/command/MoreMethods"),
                getClassFilePath("testcode/command/SubClass")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        verify(TaintDataflowEngine.writer,atLeast(2)).append(anyString());

        FindSecBugsGlobalConfig.getInstance().setDebugOutputTaintConfigs(false);
    }
}
