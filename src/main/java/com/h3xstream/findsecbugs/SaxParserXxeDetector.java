package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;

/**
 * References:
 * https://www.securecoding.cert.org/confluence/pages/viewpage.action?pageId=61702260
 */
public class SaxParserXxeDetector implements Detector {
    private BugReporter bugReporter;

    public SaxParserXxeDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {

    }

    @Override
    public void report() {

    }

}
