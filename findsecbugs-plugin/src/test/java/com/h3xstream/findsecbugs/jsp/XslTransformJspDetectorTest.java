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
package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class XslTransformJspDetectorTest extends BaseDetectorTest {

    public void detectXslTransformInJsp(String file,int[] lines) throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath(file)
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        for(int line: lines) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("JSP_XSLT")
                            .inJspFile(file)
                            .atJspLine(line)
                            .build()
            );
        }

        //Only one
        verify(reporter, times(lines.length)).doReportBug(bugDefinition().bugType("JSP_XSLT").build());
    }

    @Test
    public void testXsl1() throws Exception {
        detectXslTransformInJsp("xsl/xsl1.jsp",new int[]{2});
    }

    @Test
    public void testXsl2() throws Exception {
        detectXslTransformInJsp("xsl/xsl2.jsp",new int[]{2});
    }

    @Test
    public void testXsl3() throws Exception {
        detectXslTransformInJsp("xsl/xsl3.jsp",new int[]{}); //Empty = no bug instance
    }

    @Test
    public void testXsl4() throws Exception {
        detectXslTransformInJsp("xsl/xsl4.jsp",new int[]{}); //Empty = no bug instance
    }
}
