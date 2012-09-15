package com.h3xstream.findsecbugs.xxe;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SaxParserXxeDetectorTest extends BaseDetectorTest {

    @Test
    public void detectXxe() throws Exception {
        //Locate com.h3xstream.findbugs.test code
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
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafePrivilegedExceptionAction"),
                getClassFilePath("testcode/xxe/SaxParserSafePrivilegedExceptionAction$1")
        };

        //Run the analysis
        EasyBugReporter reporter = Mockito.spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .build()
        );
    }

    @Test
    public void safeWithUseOfEntityResolver() throws Exception {
        //Locate com.h3xstream.findbugs.test code
        String[] files = {
                getClassFilePath("testcode/xxe/SaxParserSafeEntityResolver"),
                getClassFilePath("testcode/xxe/SaxParserSafeEntityResolver$CustomResolver")
        };

        //Run the analysis
        EasyBugReporter reporter = Mockito.spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        Mockito.verify(reporter, Mockito.never()).doReportBug(
                bugDefinition()
                        .bugType("XXE")
                        .build()
        );
    }

}
