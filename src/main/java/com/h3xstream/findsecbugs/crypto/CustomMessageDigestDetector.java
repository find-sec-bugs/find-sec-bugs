package com.h3xstream.findsecbugs.crypto;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * Implementing a custom solution for message digest should not promote.
 * Well establish implementation are available from the JDK.
 */
public class CustomMessageDigestDetector implements Detector {

    private BugReporter bugReporter;

    public CustomMessageDigestDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }


    @Override
    public void visitClassContext(ClassContext classContext) {
    }

    @Override
    public void report() {
    }
}
