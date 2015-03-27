package com.h3xstream.findsecbugs.android;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WebViewJavascriptInterfaceDetectorTest  extends BaseDetectorTest {

    @Test
    public void detectWebViewJavascriptInterface() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/android/WebViewJavascriptInterfaceActivity")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions
        for(Integer line : Arrays.asList(24,25)) {
            verify(reporter).doReportBug(
                    bugDefinition() //
                            .bugType("ANDROID_WEB_VIEW_JAVASCRIPT_INTERFACE") //
                            .inClass("WebViewJavascriptInterfaceActivity") //
                            .inMethod("onCreate") //
                            .atLine(line) //
                            .build()
            );
        }

        //The count make sure no other bug are detect
        verify(reporter,times(2)).doReportBug(
                bugDefinition().bugType("ANDROID_WEB_VIEW_JAVASCRIPT_INTERFACE").build());
    }
}
