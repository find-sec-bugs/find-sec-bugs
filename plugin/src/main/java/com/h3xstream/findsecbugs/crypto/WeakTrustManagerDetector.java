/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.util.Iterator;

/**
 * <p>
 * The first reflex for developer that encounter web services that have unsigned certificate
 * is often to trust all certificates.
 * </p>
 * <p>
 * To trust everything, the standard API for SSL communication requires the implementation of a child
 * interface of "javax.net.ssl.TrustManager" (marker interface). Commonly, X509TrustManager is being used.
 * </p>
 * <a href="http://stackoverflow.com/a/1201102/89769">Sample of code being used</a>
 *
 * @see javax.net.ssl.TrustManager
 * @see javax.net.ssl.X509TrustManager
 */
public class WeakTrustManagerDetector implements Detector {

    private static final boolean DEBUG = false;
    private static final String WEAK_TRUST_MANAGER_TYPE = "WEAK_TRUST_MANAGER";
    private static final String WEAK_HOSTNAME_VERIFIER_TYPE = "WEAK_HOSTNAME_VERIFIER";
    private BugReporter bugReporter;

    public WeakTrustManagerDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        //The class extends X509TrustManager
        boolean isTrustManager = InterfaceUtils.isSubtype(javaClass, "javax.net.ssl.X509TrustManager");
        boolean isHostnameVerifier = InterfaceUtils.isSubtype(javaClass, "javax.net.ssl.HostnameVerifier");

        //Not the target of this detector
        if (!isTrustManager && !isHostnameVerifier) return;

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {
            MethodGen methodGen = classContext.getMethodGen(m);

            if (DEBUG) System.out.println(">>> Method: " + m.getName());

            if (isTrustManager &&
                    (m.getName().equals("checkClientTrusted") ||
                    m.getName().equals("checkServerTrusted") ||
                    m.getName().equals("getAcceptedIssuers"))) {


                if(isEmptyImplementation(methodGen)) {
                    bugReporter.reportBug(new BugInstance(this, WEAK_TRUST_MANAGER_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClassAndMethod(javaClass, m));
                }
            }
            else if (isHostnameVerifier && m.getName().equals("verify")) {

                if(isEmptyImplementation(methodGen)) {
                    bugReporter.reportBug(new BugInstance(this, WEAK_HOSTNAME_VERIFIER_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClassAndMethod(javaClass, m));
                }
            }

        }
    }

    /**
     * Currently the detection is pretty weak.
     * It will catch Dummy implementation that have empty method implementation
     *
     * @return If the implementation is "empty" (direct return or dummy code)
     */
    private boolean isEmptyImplementation(MethodGen methodGen){
        boolean invokeInst = false;
        boolean loadField = false;

        for (Iterator itIns = methodGen.getInstructionList().iterator();itIns.hasNext();) {
            Instruction inst = ((InstructionHandle) itIns.next()).getInstruction();
            if (DEBUG)
                System.out.println(inst.toString(true));

            if (inst instanceof InvokeInstruction) {
                invokeInst = true;
            }
            if (inst instanceof GETFIELD) {
                loadField = true;
            }
        }
        return !invokeInst && !loadField;
    }

    @Override
    public void report() {

    }
}
