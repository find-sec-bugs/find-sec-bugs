package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 *
 */
public class SpringMvcEndpointDetector implements Detector {

    private static final String SPRING_ENDPOINT_TYPE = "SPRING_ENDPOINT";
    private BugReporter bugReporter;

    public SpringMvcEndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        for (Method m : javaClass.getMethods()) {

            for (AnnotationEntry ae : m.getAnnotationEntries()) {

                if (ae.getAnnotationType().equals("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
                    bugReporter.reportBug(new BugInstance(this, SPRING_ENDPOINT_TYPE, Priorities.LOW_PRIORITY) //
                            .addClassAndMethod(javaClass, m));
                }
            }
        }
    }

    @Override
    public void report() {

    }

}
