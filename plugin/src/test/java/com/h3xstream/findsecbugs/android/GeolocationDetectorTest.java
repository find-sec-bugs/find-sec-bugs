package com.h3xstream.findsecbugs.android;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GeolocationDetectorTest  extends BaseDetectorTest {

    @Test
    public void detectGeolocationFromWebView() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/android/GeolocationActivity"),
                getClassFilePath("testcode/android/GeolocationActivity$1")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);



        //Assertions
        verify(reporter).doReportBug(
                bugDefinition() //
                        .bugType("ANDROID_GEOLOCATION") //
                        .inClass("GeolocationActivity$1") //
                        .inMethod("onGeolocationPermissionsShowPrompt") //
                        .build()
        );

        //The count make sure no other bug are detect
        verify(reporter).doReportBug(
                bugDefinition().bugType("ANDROID_GEOLOCATION").build());
    }
}
