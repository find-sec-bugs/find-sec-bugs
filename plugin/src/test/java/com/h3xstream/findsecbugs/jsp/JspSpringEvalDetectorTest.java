package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JspSpringEvalDetectorTest extends BaseDetectorTest {

    @Test
    public void jspSpringEval1_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("spring/spring_eval_1.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_SPRING_EVAL")
                        .inJspFile("spring/spring_eval_1.jsp")
                        .atJspLine(17)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_SPRING_EVAL").build());
    }

    @Test
    public void jspSpringEval2_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("spring/spring_eval_2.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_SPRING_EVAL")
                        .inJspFile("spring/spring_eval_2.jsp")
                        .atJspLine(17)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_SPRING_EVAL").build());
    }

    @Test
    public void jspSpringEval3_unsafe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("spring/spring_eval_3.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("JSP_SPRING_EVAL")
                        .inJspFile("spring/spring_eval_3.jsp")
                        .atJspLine(17)
                        .build()
        );

        //Only one
        verify(reporter).doReportBug(bugDefinition().bugType("JSP_SPRING_EVAL").build());
    }

    @Test
    public void jspSpringEval4_safe() throws Exception {
        //Locate test code
        String[] files = {
                getJspFilePath("spring/spring_eval_4_safe.jsp")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //No vulnerability should be rise
        verify(reporter,never()).doReportBug(bugDefinition().bugType("JSP_SPRING_EVAL").build());
    }
}
