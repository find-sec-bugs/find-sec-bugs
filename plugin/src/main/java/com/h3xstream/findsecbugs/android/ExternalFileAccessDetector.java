package com.h3xstream.findsecbugs.android;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class ExternalFileAccessDetector extends OpcodeStackDetector {

    private static final String EXTERNAL_FILE_ACCESS_TYPE = "EXTERNAL_FILE_ACCESS";
    private BugReporter bugReporter;

    public ExternalFileAccessDetector(BugReporter bugReporter) {
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
