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
package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SpelViewDetectorTest extends BaseDetectorTest {
        @Test
        public void detectSpelView() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/spelviewinjection/SpelView"),
                getClassFilePath("testcode/spelviewinjection/VerboseErrorController")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPEL_INJECTION")
                        .inClass("SpelView")
                        .atLine(31)
                        .build()
        );


        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SPEL_INJECTION")
                        .inClass("VerboseErrorController")
                        .atLine(41)
                        .build()
        );

        //Out of the 6 calls, 3 are suspicious
        verify(reporter,times(2)).doReportBug(
                bugDefinition()
                        .bugType("SPEL_INJECTION")
                        .build()
        );
    }
}
