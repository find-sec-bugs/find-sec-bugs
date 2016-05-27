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

import static org.mockito.Mockito.*;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

public class PredictableRandomDetectorTest extends BaseDetectorTest {

    @Test
    public void detectUsePredictableRandom() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/InsecureRandom")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions

        //1rst variation new Random()
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .inClass("InsecureRandom").inMethod("newRandomObj").atLine(9)
                        .build()
        );
        //2nd variation Math.random()
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .inClass("InsecureRandom").inMethod("mathRandom").atLine(16)
                        .build()
        );

        //Scala random number generator (mirror of java.util.Random)
        for(Integer line : Arrays.asList(30,31)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("PREDICTABLE_RANDOM_SCALA")
                            .inClass("InsecureRandom").inMethod("scalaRandom").atLine(line)
                            .build()
            );
        }


        verify(reporter, times(2)).doReportBug( //2 java api
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM")
                        .build()
        );
        verify(reporter, times(2)).doReportBug( //2 scala variations
                bugDefinition()
                        .bugType("PREDICTABLE_RANDOM_SCALA")
                        .build()
        );
    }

}
