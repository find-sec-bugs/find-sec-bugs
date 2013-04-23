package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class UnvalidatedRedirectDetector  extends OpcodeStackDetector {

    private static final String UNVALIDATED_REDIRECT_TYPE = "UNVALIDATED_REDIRECT";

    private BugReporter bugReporter;

    public UnvalidatedRedirectDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

    }
}
