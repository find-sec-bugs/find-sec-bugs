package com.h3xstream.findsecbugs.kotlin;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class StringManipulationCommandInjectionTest extends BaseDetectorTest {


    @Test
    public void detectCommandInjection() throws Exception {

        //Locate test code
        String[] files = {
                getClassFilePath("com/h3xstream/findsecbugs/command/StringManipulationCommandInjection")
        };


        //Run the analysis
        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

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
