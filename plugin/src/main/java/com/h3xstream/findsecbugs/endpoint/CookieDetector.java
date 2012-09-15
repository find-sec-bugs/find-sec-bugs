package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class CookieDetector extends OpcodeStackDetector {

    private static final String COOKIE_USAGE_TYPE = "COOKIE_USAGE";

    private BugReporter bugReporter;

    public CookieDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("javax/servlet/http/Cookie")
            && (getNameConstantOperand().equals("getName") || getNameConstantOperand().equals("getValue") ||
            getNameConstantOperand().equals("getPath"))) {

            bugReporter.reportBug(new BugInstance(this, COOKIE_USAGE_TYPE, Priorities.LOW_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
        }
    }
}
