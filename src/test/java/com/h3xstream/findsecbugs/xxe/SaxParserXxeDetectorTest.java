package com.h3xstream.findsecbugs.xxe;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SaxParserXxeDetectorTest extends BaseDetectorTest {

    @Test
    public void detectXxe() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserVulnerable")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .inClass("SaxParserVulnerable").inMethod("receiveXMLStream").atLine(22)
                        .build()
        );
    }

    @Test
    public void safeWithUseOfPrivilegeExceptionAction() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafePrivilegedExceptionAction"),
                getClassFilePath("testcode/xxe/SaxParserSafePrivilegedExceptionAction$1")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .build()
        );
    }

    @Test
    public void safeWithUseOfEntityResolver() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafeEntityResolver"),
                getClassFilePath("testcode/xxe/SaxParserSafeEntityResolver$CustomResolver")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .build()
        );
    }

}
