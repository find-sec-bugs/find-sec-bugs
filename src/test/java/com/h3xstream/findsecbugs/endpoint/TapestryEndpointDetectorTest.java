package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TapestryEndpointDetectorTest extends BaseDetectorTest {

    /**
     * This first code sample has some Annotation and Type imports.
     * @throws Exception
     */
    @Test
    public void detectTapestryPageWithFrameworkReference() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/pages/sub/TapestryPage")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("TAPESTRY_ENDPOINT")
                        .inClass("TapestryPage")
                        .build()
        );
    }

    /**
     * This code sample is a plain POJO. The package name will trigger the pointer.
     * @throws Exception
     */
    @Test
    public void detectTapestryPagePojo() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/pages/Index")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("TAPESTRY_ENDPOINT")
                        .inClass("Index")
                        .build()
        );
    }

    /**
     * Make sure that class not in a package containing ".pages"
     * as not mark as endpoint.
     */
    @Test
    public void noFalsePositiveOnOtherClass() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/JaxRsService"),
                getClassFilePath("testcode/JaxWsService")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //
        verify(reporter, never()).doReportBug(
                bugDefinition()
                        .bugType("TAPESTRY_ENDPOINT")
                        .build()
        );
    }
}
