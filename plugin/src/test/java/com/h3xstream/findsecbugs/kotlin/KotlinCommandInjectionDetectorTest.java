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
import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class KotlinCommandInjectionDetectorTest extends BaseDetectorTest {

    @Test
    public void detectCommandInjection() throws Exception {

        FindSecBugsGlobalConfig.getInstance().setDebugPrintInstructionVisited(true);
        FindSecBugsGlobalConfig.getInstance().setDebugTaintState(true);

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
                        .inClass("IdentityFunctionCommandInjection").atLine(25)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after always false not filter
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(31)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after identity function with run
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(37)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after identity function with let
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(43)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after identity function with apply
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(47)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after drop while always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(53)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after drop last while always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(59)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after take while always true
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(65)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after take last while always true then reversed
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(71)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after trim always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(77)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after trim end always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(83)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after trim start always false
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(89)
                        .withPriority("Medium")
                        .build()
        );

        // tained input executed after also does nothing
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("IdentityFunctionCommandInjection").atLine(95)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after applying suffix with plus
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(25)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after (de)capitalisation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(31)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after first characters removed
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(37)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after last characters removed
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(43)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after front padding
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(49)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after end padding
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(55)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after indentation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(61)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove prefix
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(67)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove range
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(73)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove suffix
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(79)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after remove surrounding
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(85)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replacement
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(91)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace first
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(97)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace after
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(103)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace after last
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(109)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace before
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(115)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after replace before last
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(121)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after reversal
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(127)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(133)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming indent
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(139)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming start
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(145)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after trimming end
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(151)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after front truncation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(157)
                        .withPriority("Medium")
                        .build()
        );

        //tained input executed after end truncation
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("COMMAND_INJECTION")
                        .inClass("StringManipulationCommandInjection").atLine(163)
                        .withPriority("Medium")
                        .build()
        );
    }
}
