package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.test.BaseDetectorTest;
import edu.umd.cs.findbugs.test.EasyBugReporter;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class WeakTrustManagerDetectorTest extends BaseDetectorTest {


    @Test
    public void detectWeakTrustManager() throws Exception {
        //Locate test code
        String[] files = {
                getClassFilePath("testcode/trustmanager/WeakTrustManager"),
                getClassFilePath("testcode/trustmanager/WeakTrustManager$TrustManagerInnerClass"),
                getClassFilePath("testcode/trustmanager/WeakTrustManager$1"),

                //The following should not trigger any bug
                getClassFilePath("testcode/trustmanager/WeakTrustManager$FakeImpl"),
                getClassFilePath("testcode/trustmanager/KeyStoresTrustManager")
        };

        //Run the analysis
        EasyBugReporter reporter = spy(new EasyBugReporter());
        analyze(files, reporter);

        //Assertions

        //Anonymous class

        verify(reporter,atLeastOnce()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$1").inMethod("getAcceptedIssuers")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$1").inMethod("checkServerTrusted")
                        .build()
        );

        //Inner class
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$TrustManagerInnerClass").inMethod("checkServerTrusted")
                        .build()
        );
        verify(reporter).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("WeakTrustManager$TrustManagerInnerClass").inMethod("getAcceptedIssuers")
                        .build()
        );

        //The KeyStoresTrustManager impl. should not trigger any report
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("KeyStoresTrustManager")
                .build()
        );

        //The FakeImpl should not trigger any report (doesn't implements the analysed Interface)
        verify(reporter,never()).doReportBug(
                bugDefinition()
                        .bugType("WEAK_TRUST_MANAGER")
                        .inClass("FakeImpl")
                .build()
        );
    }
}
