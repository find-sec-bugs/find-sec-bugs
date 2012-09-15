package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class StaticIvDetector extends OpcodeStackDetector {

    private static final String STATIC_IV_TYPE = "STATIC_IV";
    private BugReporter bugReporter;

    public StaticIvDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        printOpCode(seen);

        if (seen == Constants.INVOKESPECIAL && getClassConstantOperand().equals("javax/crypto/spec/IvParameterSpec") &&
                getNameConstantOperand().equals("<init>")) {
            //System.out.println(stack.getStackDepth());
        }
    }
}
