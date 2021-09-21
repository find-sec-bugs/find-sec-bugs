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
package com.h3xstream.findsecbugs;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

import java.io.File;
import java.net.URL;

public class PossibleUntrustedObjectInjectionTest extends BaseDetectorTest {

    @Test
    public void testBadParameter() throws Exception {
        FindSecBugsGlobalConfig.getInstance().setTaintedPublicMethodParameters(true);

        verifyOBJBug("badOpenFile", 13, true);
        
        FindSecBugsGlobalConfig.getInstance().setTaintedPublicMethodParameters(false);
    }

    @Test
    public void testGoodParameter() throws Exception {
        FindSecBugsGlobalConfig.getInstance().setTaintedPublicMethodParameters(true);

        verifyOBJBug("goodOpenFile", 28, false);

        FindSecBugsGlobalConfig.getInstance().setTaintedPublicMethodParameters(false);
    }

    @Test
    public void testBadReturnValue() throws Exception {
        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/taintanalysis/CustomConfig.txt");
        File configFile = new File(configUrl.toURI());

        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(configFile.getCanonicalPath());

        verifyOBJBug("badOpenFile2", 43, true);

        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile("");        
    }

    @Test
    public void testGoodReturnValue() throws Exception {
        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/taintanalysis/CustomConfig.txt");
        File configFile = new File(configUrl.toURI());

        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(configFile.getCanonicalPath());

        verifyOBJBug("goodOpenFile2", 60, false);

        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile("");
    }

    private void verifyOBJBug(String methodName, int line, boolean isBug) throws Exception {
        //Locate test code
        String[] files = {
            getClassFilePath("testcode/PossibleUntrustedObjectInjection"),
            getClassFilePath("testcode/PossibleUntrustedObjectInjection$1"),
            getClassFilePath("testcode/PossibleUntrustedObjectInjection$2"),
            getClassFilePath("testcode/PossibleUntrustedObjectInjection$3"),
            getClassFilePath("testcode/PossibleUntrustedObjectInjection$4"),
            getClassFilePath("testcode/PossibleUntrustedObjectInjection$5")
    };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, isBug ? times(1) : never()).doReportBug(
            bugDefinition()
                    .bugType("POSSIBLE_UNTRUSTED_OBJECT_INJECTION")
                    .inClass("PossibleUntrustedObjectInjection")
                    .inMethod(methodName)
                    .withPriority("Medium")
                    .atLine(line)
                    .build()
        );
    }
}
