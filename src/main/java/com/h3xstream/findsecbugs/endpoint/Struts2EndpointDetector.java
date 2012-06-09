package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;

public class Struts2EndpointDetector implements Detector {

    private BugReporter bugReporter;

    public Struts2EndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {

    }

    @Override
    public void report() {

    }
}
