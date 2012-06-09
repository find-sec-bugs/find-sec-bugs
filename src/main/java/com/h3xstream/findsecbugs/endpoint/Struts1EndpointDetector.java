package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;

public class Struts1EndpointDetector implements Detector {

    public static final String STRUTS1_ENDPOINT_TYPE = "STRUTS1_ENDPOINT";

    private BugReporter bugReporter;

    public Struts1EndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        if ("org.apache.struts.action.Action".equals(javaClass.getSuperclassName())) {
            bugReporter.reportBug(new BugInstance(this, STRUTS1_ENDPOINT_TYPE, LOW_PRIORITY) //
                    .addClass(javaClass));
        }
    }

    @Override
    public void report() {

    }
}
