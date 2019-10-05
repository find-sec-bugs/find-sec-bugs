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
import edu.umd.cs.findbugs.SystemProperties;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class TaintCustomSinksTest extends BaseDetectorTest {


    @Test
    public void withCustomConfigEnableWithEnvVariable() throws Exception {
        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/taintanalysis/XPathCustomSinksConfig.txt");
        File configFile = new File(configUrl.toURI());

        SystemProperties.setProperty("findsecbugs.injection.customconfigfile.XPathInjectionDetector",configFile.getCanonicalPath()+"|XPATH_INJECTION");

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathJavaxCustomSinks.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,times(1)).doReportBug(
                bugDefinition().bugType("XPATH_INJECTION")
                        .inClass("XPathJavaxCustomSinks").inMethod("main")
                        .build()
        );

        //Avoid potential conflicts with other tests.
        //There will still remain a custom sink load if the test suite does not reload all classes. (Minor artifact)
        SystemProperties.setProperty("findsecbugs.injection.customconfigfile.XPathInjectionDetector","");

    }

    /**
     * When editing this test case, make sure it failed when adding a suffix to the property.
     *
     * We can't reload the configuration as XPath and test for failure because, sink is likely to have been load in the previous test case.
     * That's why the same configuration file is reload but with the LDAP Injection detector.
     *
     * @throws Exception
     */
    @Test
    public void withCustomConfigEnableWithEnvVariableNotLoaded() throws Exception {
        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/taintanalysis/XPathCustomSinksConfig.txt");
        File configFile = new File(configUrl.toURI());

        SystemProperties.setProperty("findsecbugs.injection.customconfigfile.LdapInjectionDetector",configFile.getCanonicalPath()); //Missing +"|LDAP_INJECTION"

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xpath/XPathJavaxCustomSinks.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("LDAP_INJECTION")
                        .inClass("XPathJavaxCustomSinks").inMethod("main")
                        .build()
        );

        SystemProperties.setProperty("findsecbugs.injection.customconfigfile.LdapInjectionDetector",""); //Avoid potential conflicts with other tests.
    }
}
