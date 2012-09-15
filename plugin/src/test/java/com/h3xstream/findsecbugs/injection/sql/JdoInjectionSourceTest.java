package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JdoInjectionSourceTest extends BaseDetectorTest {

    @Test
    public void detectInjection() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/sqli/JdoSql")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        for (Integer line : Arrays.asList(20, 22)) {
            verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SQL_INJECTION")
                        .inClass("JdoSql").inMethod("testJdoQueries").atLine(line)
                        .build()
            );
        }

        //Only the previous 2 cases should be marked as vulnerable
        verify(reporter, times(2)).doReportBug(
            bugDefinition()
                    .bugType("SQL_INJECTION")
                    .build()
        );
    }
}
