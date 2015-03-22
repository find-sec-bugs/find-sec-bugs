package com.h3xstream.findsecbugs.android;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class BroadcastDetector extends OpcodeStackDetector {

    private static final String ANDROID_BROADCAST_TYPE = "ANDROID_BROADCAST";
    private BugReporter bugReporter;

    public BroadcastDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        printOpCode(seen);

        if (seen == Constants.INVOKESPECIAL && getClassConstantOperand().equals("java/net/Socket") &&
                getNameConstantOperand().equals("<init>")) {
        }
    }
}
