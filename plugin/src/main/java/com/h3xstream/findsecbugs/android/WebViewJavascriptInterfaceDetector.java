package com.h3xstream.findsecbugs.android;

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class WebViewJavascriptInterfaceDetector extends OpcodeStackDetector {

    private static final String ANDROID_WEB_VIEW_INTERFACE_TYPE = "ANDROID_WEB_VIEW_JAVASCRIPT_INTERFACE";
    private BugReporter bugReporter;

    public WebViewJavascriptInterfaceDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("android/webkit/WebView") &&
                getNameConstantOperand().equals("addJavascriptInterface")) {

            bugReporter.reportBug(new BugInstance(this, ANDROID_WEB_VIEW_INTERFACE_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }

    }
}
