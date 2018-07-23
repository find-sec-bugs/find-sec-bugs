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
package com.h3xstream.findsecbugs.kotlin;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class KotlinCommandInjectionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommandInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/command/IdentityFunctionCommandInjection"),
                getClassFilePath("com/h3xstream/findsecbugs/command/StringManipulationCommandInjection")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        // tained input executed after always true filter
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(26)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after always false not filter
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(32)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after identity function with run
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(38)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after identity function with let
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(44)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after identity function with apply
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(48)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after drop while always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(54)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after drop last while always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(60)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after take while always true
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(66)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after take last while always true then reversed
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(72)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after trim always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(78)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after trim end always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(84)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after trim start always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(90)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after also does nothing
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(96)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after applying suffix with plus
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(26)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after (de)capitalisation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(32)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after first characters removed
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(38)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after last characters removed
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(44)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after front padding
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(50)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after end padding
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(56)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after indentation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(62)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove prefix
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(68)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove range
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(74)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove suffix
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(80)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove surrounding
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(86)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replacement
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(92)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace first
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(98)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace after
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(104)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace after last
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(110)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace before
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(116)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace before last
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(122)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after reversal
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(128)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(134)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming indent
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(140)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming start
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(146)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming end
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(152)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after front truncation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(158)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after end truncation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(164)
                        .withPriority("Medium")
                        .build()
        );
    }
}
