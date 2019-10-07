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

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.*;

public class TaintCustomConfigTest extends BaseDetectorTest {

    @Test
    public void withCustomConfigEnable() throws Exception {
        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/taintanalysis/CustomConfig.txt");
        File configFile = new File(configUrl.toURI());

        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(configFile.getCanonicalPath());

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathJavaxCustomSafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition().bugType("XPATH_INJECTION")
                        .inClass("XPathJavaxCustomSafe").inMethod("main")
                        .build()
        );

        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(""); //Avoid potential conflicts with other tests.
    }

    @Test
    public void withCustomConfigDisable() throws Exception {
//        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/taintanalysis/CustomConfig.txt");
//        File configFile = new File(configUrl.toURI());
//
//        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(configFile.getCanonicalPath());

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathJavaxCustomSafe")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,times(2)).doReportBug(
                bugDefinition().bugType("XPATH_INJECTION")
                        .inClass("XPathJavaxCustomSafe").inMethod("main")
                        .build()
        );
    }
}
