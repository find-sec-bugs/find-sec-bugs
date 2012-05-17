package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * JAX-RS (JSR224) defines an api for Web service.
 */
public class JaxWsEndpointDetector implements Detector {

    private static final boolean DEBUG = false;
    private static final String JAXWS_ENDPOINT_TYPE = "JAXWS_ENDPOINT";
    private BugReporter bugReporter;

    public JaxWsEndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        for (Method m : javaClass.getMethods()) {

            for (AnnotationEntry ae : m.getAnnotationEntries()) {

                //Every method mark with @javax.jws.WebMethod is mark as an Endpoint
                if (ae.getAnnotationType().equals("Ljavax/jws/WebMethod;")) {
                    bugReporter.reportBug(new BugInstance(this, JAXWS_ENDPOINT_TYPE, LOW_PRIORITY) //
                            .addClassAndMethod(javaClass, m));
                }
            }
        }
    }

    @Override
    public void report() {

    }
}
