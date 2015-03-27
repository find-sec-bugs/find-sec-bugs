package com.h3xstream.findsecbugs.android;

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class WebViewJavascriptEnabledDetector extends OpcodeStackDetector {

    private static final String ANDROID_WEB_VIEW_JAVASCRIPT_TYPE = "ANDROID_WEB_VIEW_JAVASCRIPT";
    private BugReporter bugReporter;

    public WebViewJavascriptEnabledDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("android/webkit/WebSettings") &&
                getNameConstantOperand().equals("setJavaScriptEnabled")) {
            OpcodeStack.Item item = stack.getStackItem(0); //First item on the stack is the last
            if(StackUtils.isConstantInteger(item)) {
                Integer value = (Integer) item.getConstant();
                if(value == null || value == 1) {
                    bugReporter.reportBug(new BugInstance(this, ANDROID_WEB_VIEW_JAVASCRIPT_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this));
                }
            }
        }
    }
}
