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
package com.h3xstream.findsecbugs.scala;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PlayUnvalidatedRedirectDetectorTest extends BaseDetectorTest {

    @Test
    public void detectUsePredictableRandom() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("bytecode_samples/scala_play_openredirect.jar")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PLAY_UNVALIDATED_REDIRECT")
                        .inClass("RedirectController").inMethod("redirect1").atLine(8)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PLAY_UNVALIDATED_REDIRECT")
                        .inClass("RedirectController").inMethod("redirect2").atLine(12)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PLAY_UNVALIDATED_REDIRECT")
                        .inClass("RedirectController").inMethod("seeOther1").atLine(17)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PLAY_UNVALIDATED_REDIRECT")
                        .inClass("RedirectController").inMethod("movedPermanently1").atLine(21)
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("PLAY_UNVALIDATED_REDIRECT")
                        .inClass("RedirectController").inMethod("temporaryRedirect1").atLine(25)
                        .build()
        );
    }


}
