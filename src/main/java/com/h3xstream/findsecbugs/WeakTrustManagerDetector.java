package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;

/**
 * The first reflex for developer that encounter web services that have unsigned certificate
 * is often to trust all certificates.
 * <p/>
 * To trust everything, the standard API for SSL communication requires the implementation of a child
 * interface of "javax.net.ssl.TrustManager" (marker interface). Commonly, X509TrustManager is being used.
 * <p/>
 * <a href="http://stackoverflow.com/a/1201102/89769">Sample of code being used</a>
 *
 * @see javax.net.ssl.TrustManager
 * @see javax.net.ssl.X509TrustManager
 */
public class WeakTrustManagerDetector implements Detector {

    private static final boolean DEBUG = false;
    private static final String WEAK_TRUST_MANAGER_TYPE = "WEAK_TRUST_MANAGER";
    private BugReporter bugReporter;

    public WeakTrustManagerDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        //The class extends X509TrustManager
        boolean isTrustManager = false;
        String[] interfaces = javaClass.getInterfaceNames();
        for (String name : interfaces) {
            if (name.equals("javax.net.ssl.X509TrustManager")) {
                isTrustManager = true;
                break;
            }
        }

        //Not the target of this detector
        if (!isTrustManager) return;

        System.out.println(javaClass.getClassName());
        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {
            MethodGen methodGen = classContext.getMethodGen(m);

            if(DEBUG) System.out.println(">>> Method: " + m.getName());

            //The presence of checkClientTrusted is not enforce for the moment
            if (!m.getName().equals("checkServerTrusted") &&
                    !m.getName().equals("getAcceptedIssuers")) {
                continue;
            }

            //Currently the detection is pretty weak.
            //It will catch Dummy implementation that have empty method implementation
            boolean invokeInst = false;
            boolean loadField = false;

            for (Instruction inst : methodGen.getInstructionList().getInstructions()) {
                if (DEBUG)
                    System.out.println(inst.toString(true));

                if (inst instanceof InvokeInstruction) {
                    invokeInst = true;
                }
                if (inst instanceof GETFIELD) {
                    loadField = true;
                }
            }

            if (!invokeInst && !loadField) {
                bugReporter.reportBug(new BugInstance(this, WEAK_TRUST_MANAGER_TYPE, NORMAL_PRIORITY) //
                        .addClassAndMethod(javaClass, m));
            }
        }
    }

    @Override
    public void report() {

    }
}
