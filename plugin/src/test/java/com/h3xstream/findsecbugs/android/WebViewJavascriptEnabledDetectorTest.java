package com.h3xstream.findsecbugs.android;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class WebViewJavascriptEnabledDetectorTest extends BaseDetectorTest {

    @Test
    public void detectWebViewJavascriptEnabled() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/android/WebViewJavascriptEnabledActivity")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        verify(reporter).doReportBug(
                bugDefinition() //
                        .bugType("ANDROID_WEB_VIEW_JAVASCRIPT") //
                        .inClass("WebViewJavascriptEnabledActivity") //
                        .inMethod("onCreate") //
                        .atLine(17) //
                        .build()
        );

        //The count make sure no other bug are detect
        verify(reporter).doReportBug(
                bugDefinition().bugType("ANDROID_WEB_VIEW_JAVASCRIPT").build());
    }
}
