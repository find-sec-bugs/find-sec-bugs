package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class Struts2EndpointDetector implements Detector {

    private static final String STRUTS2_ENDPOINT_TYPE = "STRUTS2_ENDPOINT";

    private BugReporter bugReporter;

    public Struts2EndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        for(Method m : javaClass.getMethods()) {
            if("execute".equals(m.getName()) && "()Ljava/lang/String;".equals(m.getSignature())) {
                bugReporter.reportBug(new BugInstance(this, STRUTS2_ENDPOINT_TYPE, LOW_PRIORITY) //
                                    .addClass(javaClass));
            }
        }
    }

    @Override
    public void report() {

    }
}
