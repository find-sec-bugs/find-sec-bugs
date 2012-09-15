package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class NullCipherDetector extends OpcodeStackDetector {

    private static final String NULL_CIPHER_TYPE = "NULL_CIPHER";

    private BugReporter bugReporter;

    public NullCipherDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode( seen );
        if (seen == Constants.INVOKESPECIAL && getClassConstantOperand().equals("javax/crypto/NullCipher") &&
                getNameConstantOperand().equals("<init>")) {

            bugReporter.reportBug(new BugInstance(this, NULL_CIPHER_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass( this ).addMethod(this).addSourceLine(this));
        }
    }
}
