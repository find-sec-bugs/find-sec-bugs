package com.h3xstream.findsecbugs.xpath;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class XPathInjectionApacheXPathApiDetector extends OpcodeStackDetector {

    private BugReporter bugReporter;

    public XPathInjectionApacheXPathApiDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

    }
}
