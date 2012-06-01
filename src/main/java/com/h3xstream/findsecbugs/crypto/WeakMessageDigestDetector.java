package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class WeakMessageDigestDetector extends OpcodeStackDetector {

    private static String WEAK_MESSAGE_DIGEST_TYPE = "WEAK_MESSAGE_DIGEST";

    private BugReporter bugReporter;

    public WeakMessageDigestDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == INVOKESTATIC && getClassConstantOperand().equals("java/security/MessageDigest") &&
                getNameConstantOperand().equals("getInstance") &&
                getSigConstantOperand().equals("(Ljava/lang/String;)Ljava/security/MessageDigest;")) {

            //Extract the value being push..
            OpcodeStack.Item top = stack.getStackItem(0);
            String algorithm = (String) top.getConstant();

            if ("MD2".equals(algorithm) || "MD5".equals(algorithm)) {
                bugReporter.reportBug(new BugInstance(this, WEAK_MESSAGE_DIGEST_TYPE, NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this) //
                        .addString(algorithm));
            }

        }
    }
}
