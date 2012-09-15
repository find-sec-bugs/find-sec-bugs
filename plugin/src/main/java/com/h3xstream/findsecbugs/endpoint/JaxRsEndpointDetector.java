package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import javax.lang.model.element.AnnotationMirror;

/**
 * JAX-RS (JSR311) defines an api for REST service.
 */
public class JaxRsEndpointDetector implements Detector {
    private static final boolean DEBUG = false;
    private static final String JAXRS_ENDPOINT_TYPE = "JAXRS_ENDPOINT";
    private BugReporter bugReporter;

    public JaxRsEndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        for (Method m : javaClass.getMethods()) {

            for (AnnotationEntry ae : m.getAnnotationEntries()) {

                //Every method mark with @javax.ws.rs.Path is mark as an Endpoint
                if (ae.getAnnotationType().equals("Ljavax/ws/rs/Path;")) {
                    bugReporter.reportBug(new BugInstance(this, JAXRS_ENDPOINT_TYPE, Priorities.LOW_PRIORITY) //
                            .addClassAndMethod(javaClass, m));
                }
            }
        }
    }

    @Override
    public void report() {

    }
}
