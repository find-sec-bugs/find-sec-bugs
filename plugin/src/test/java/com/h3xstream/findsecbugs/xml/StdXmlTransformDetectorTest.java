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
package com.h3xstream.findsecbugs.xml;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class StdXmlTransformDetectorTest extends BaseDetectorTest {

    @BeforeClass
    public void traceCalls() {
//        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInvocationVisited(true);
//        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
    }

    @Test
    public void xslt1SafeStaticResource() throws Exception {
        xsltUnsafeTemplate("xslt1SafeStaticResource", new int[] {});
    }

    @Test
    public void xslt2UnsafeResource() throws Exception {
        xsltUnsafeTemplate("xslt2UnsafeResource", new int[] {34});
    }

    @Test
    public void xslt3UnsafeResource() throws Exception {
        xsltUnsafeTemplate("xslt3UnsafeResource", new int[] {43});
    }

    @Test
    public void xslt4UnsafeResource() throws Exception {
        xsltUnsafeTemplate("xslt4UnsafeResource", new int[] {55});
    }

    @Test
    public void xslt5SafeResource() throws Exception {
        xsltUnsafeTemplate("xslt5SafeResource", new int[] {});
    }

    private void xsltUnsafeTemplate(String method, int[] lines) throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xsl/StdXmlTransform")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        for(int line : lines) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("MALICIOUS_XSLT")
                            .inClass("StdXmlTransform").inMethod(method).atLine(line)
                            .build()
            );
        }

        verify(reporter, times(lines.length)).doReportBug(
                bugDefinition()
                        .bugType("MALICIOUS_XSLT")
                        .inClass("StdXmlTransform").inMethod(method)
                        .build()
        );
    }
}
