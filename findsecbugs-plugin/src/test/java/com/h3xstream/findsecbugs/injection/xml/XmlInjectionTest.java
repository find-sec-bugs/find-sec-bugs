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
package com.h3xstream.findsecbugs.injection.xml;

import static org.mockito.Mockito.*;

import java.io.File;
import java.net.URL;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;

import org.testng.annotations.Test;

public class XmlInjectionTest extends BaseDetectorTest {
    @Test
    public void testBadParameter() throws Exception {
        verifySECXMLBug("badXmlStringParam", "Medium", true);
    }

    @Test
    public void testGoodParameter() throws Exception {
        verifySECXMLBug("goodXmlStringParam", "Medium", false);
    }

    @Test
    public void testBadMethod() throws Exception {
        verifySECXMLBug("badXmlStringFunction", "Medium", true);
    }

    @Test
    public void testGoodMethod() throws Exception {
        verifySECXMLBug("goodXmlStringFunction1", "Medium", false);
        verifySECXMLBug("goodXmlStringFunction2", "Medium", false);
        verifySECXMLBug("goodXmlStringFunction3", "Medium", false);
    }


    private void verifySECXMLBug(String methodName, String priority, boolean isBug) throws Exception {
//        URL configUrl = this.getClass().getResource("/com/h3xstream/findsecbugs/injection/xml/CustomConfig.txt");
//        File configFile = new File(configUrl.toURI());
//
//        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile(configFile.getCanonicalPath());

        //Locate test code
        String[] files = {
            getClassFilePath("testcode/xml/XmlInjection.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter, isBug ? times(1) : never()).doReportBug(
            bugDefinition()
                    .bugType("POTENTIAL_XML_INJECTION")
                    .inClass("XmlInjection")
                    .inMethod(methodName)
                    //.withPriority(priority)
                    .build()
        );

        FindSecBugsGlobalConfig.getInstance().setCustomConfigFile("");
    }
}
