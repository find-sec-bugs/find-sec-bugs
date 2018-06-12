package com.h3xstream.findsecbugs.kotlin;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KotlinHardcodedPasswordEqualsDetectorTest extends BaseDetectorTest {

    @Test
    public void detectHardCodePasswordsWithEquals() throws Exception {
        String[] files = {
                getClassFilePath("bytecode_samples/kotlin_hardcoded_password_equals.jar"),
        };

        EasyBugReporter reporter = spy(new BaseDetectorTest.SecurityReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(15, 24, 33)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("KOTLIN_HARD_CODE_PASSWORD")
                            .inClass("EqualsPasswordField").atLine(line)
                            .build()
            );
        }

        for (Integer line : Arrays.asList(84, 93, 102)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("KOTLIN_HARD_CODE_PASSWORD")
                            .inClass("HardcodedPasswordKt").atLine(line)
                            .build()
            );
        }

        verify(reporter, times(6)).doReportBug(bugDefinition().bugType("KOTLIN_HARD_CODE_PASSWORD").build());
    }
}
