package com.h3xstream.findsecbugs.xml;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class EntityResolverUnsafeUsageDetectorTest extends BaseDetectorTest {

    @Test
    public void detectBadKeySize() throws Exception {
        String[] files = {
                getClassFilePath("testcode/xml/UnsafeEntityResolver"),
                getClassFilePath("testcode/xml/SafeEntityResolver")
        };

        EasyBugReporter reporter = spy(new SecurityReporter());
        analyze(files, reporter);

        verifyMethod(reporter, "methodWithArgument");
        verifyMethod(reporter, "<init>");
        for (int line : Arrays.asList(23, 24, 28)) {
            verifyLine(reporter, line);
        }
        verify(reporter, times(5)).doReportBug(
                bugDefinition().bugType("ENTITY_RESOLVER_UNSAFE_USAGE").build());
    }

    private void verifyMethod(EasyBugReporter reporter, String methodName) {
        verify(reporter).doReportBug(
                bugDefinition().bugType("ENTITY_RESOLVER_UNSAFE_USAGE")
                        .inClass("UnsafeEntityResolver")
                        .inMethod(methodName)
                        .build()
        );
    }

    private void verifyLine(EasyBugReporter reporter, int line) {
        verify(reporter).doReportBug(
                bugDefinition().bugType("ENTITY_RESOLVER_UNSAFE_USAGE")
                        .inClass("UnsafeEntityResolver")
                        .atLine(line)
                        .build()
        );
    }
}
