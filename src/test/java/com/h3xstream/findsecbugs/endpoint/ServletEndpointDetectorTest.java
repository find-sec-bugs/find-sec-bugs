package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ServletEndpointDetectorTest extends BaseDetectorTest {

    @Test
    public void detectServletVariousInputs() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/BasicServlet")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);


        //Assertions

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_CONTENT_TYPE")
                        .inClass("BasicServlet").inMethod("doGet").atLine(16)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_SERVER_NAME")
                        .inClass("BasicServlet").inMethod("doGet").atLine(17)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_SESSION_ID")
                        .inClass("BasicServlet").inMethod("doGet").atLine(19)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_QUERY_STRING")
                        .inClass("BasicServlet").inMethod("doGet").atLine(20)
                        .build()
        );

        //Headers

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_HEADER_REFERER")
                        .inClass("BasicServlet").inMethod("doGet").atLine(22)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_SERVER_NAME")
                        .inClass("BasicServlet").inMethod("doGet").atLine(24)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_HEADER_USER_AGENT")
                        .inClass("BasicServlet").inMethod("doGet").atLine(25)
                        .build()
        );

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("SERVLET_HEADER")
                        .inClass("BasicServlet").inMethod("doGet").atLine(26)
                        .build()
        );

        for (Integer line : Arrays.asList(35, 37, 39, 41)) {
            verify(reporter).doReportBug(
                    bugDefinition()
                            .bugType("SERVLET_PARAMETER")
                            .inClass("BasicServlet").inMethod("useParameters").atLine(line)
                            .build()
            );
        }
    }

}
