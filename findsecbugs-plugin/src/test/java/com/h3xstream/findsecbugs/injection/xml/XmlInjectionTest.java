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
import java.util.Arrays;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;

import org.testng.annotations.Test;

public class XmlInjectionTest extends BaseDetectorTest {

    @Test
    private void testingVariousXmlConcatenation() throws Exception {

        //Locate test code
        String[] files = {
            getClassFilePath("testcode/xml/XmlInjection.java")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);


        for(String tpTest : Arrays.asList("badXmlStringParam", "badXmlStringFunction")) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("POTENTIAL_XML_INJECTION")
                            .inClass("XmlInjection")
                            .inMethod(tpTest)
                            .build()
            );
        }

        for(String tpTest : Arrays.asList("goodXmlStringParam", "goodXmlStringFunction1", "goodXmlStringFunction2",
                "goodXmlStringFunction3")) {
            verify(reporter, never()).doReportBug(
                    bugDefinition()
                            .bugType("POTENTIAL_XML_INJECTION")
                            .inClass("XmlInjection")
                            .inMethod(tpTest)
                            .build()
            );
        }

        //Only to TP in total
        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("POTENTIAL_XML_INJECTION")
                        .inClass("XmlInjection")
                        .build()
        );

    }
}
