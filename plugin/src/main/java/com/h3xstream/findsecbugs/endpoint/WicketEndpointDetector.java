package com.h3xstream.findsecbugs.endpoint;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;

/**
 * Identify endpoints using the web framework Wicket.
 * <p>
 * <a href="http://wicket.apache.org/">Official website</a>
 */
public class WicketEndpointDetector implements Detector {

    private static final String WICKET_ENDPOINT_TYPE = "WICKET_ENDPOINT";

    private BugReporter bugReporter;

    public WicketEndpointDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        String superClassName = javaClass.getSuperclassName();
        if ("org.apache.wicket.markup.html.WebPage".equals(superClassName)) {
            bugReporter.reportBug(new BugInstance(this, WICKET_ENDPOINT_TYPE, Priorities.LOW_PRIORITY) //
                .addClass(javaClass));
            return;
        }
    }

    @Override
    public void report() {

    }
}
