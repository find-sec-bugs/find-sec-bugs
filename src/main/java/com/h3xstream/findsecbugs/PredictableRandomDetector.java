package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class PredictableRandomDetector extends OpcodeStackDetector {

    private static final String PREDICTABLE_RANDOM_TYPE = "PREDICTABLE_RANDOM";

    private BugReporter bugReporter;

    public PredictableRandomDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == INVOKESPECIAL && getClassConstantOperand().equals("java/util/Random")
                && getNameConstantOperand().equals("<init>")) {
            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.util.Random"));

        } else if (seen == INVOKESTATIC && getClassConstantOperand().equals("java/lang/Math")
                && getNameConstantOperand().equals("random")) {
            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_TYPE, NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.lang.Math.random()"));
        }
    }

}
