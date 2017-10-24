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

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.Instruction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;


public class InsecureSmtpSslDetector implements Detector {
    private static final String INSECURE_SMTP_SSL = "INSECURE_SMTP_SSL";

    private static final List<String> INSECURE_APIS = Arrays.asList(
            "org.apache.commons.mail.Email", //
            "org.apache.commons.mail.HtmlEmail", //
            "org.apache.commons.mail.ImageHtmlEmail", //
            "org.apache.commons.mail.MultiPartEmail");

    private final BugReporter bugReporter;

    public InsecureSmtpSslDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        for (Method m : javaClass.getMethods()) {
            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        HashMap<Location, String> sslConnMap = new HashMap<Location, String>();
        HashSet<String> sslCertVerSet = new HashSet<String>();
        String hostName = null;

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();

            Instruction inst = location.getHandle().getInstruction();

            if (inst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                if (INSECURE_APIS.contains(invoke.getClassName(cpg)) &&
                        "setHostName".equals(invoke.getMethodName(cpg))) {
                    hostName = ByteCode.getConstantLDC(location.getHandle().getPrev(), cpg, String.class);
                }
                if (INSECURE_APIS.contains(invoke.getClassName(cpg)) &&
                        "setSSLOnConnect".equals(invoke.getMethodName(cpg))) {
                    Integer sslOn = ByteCode.getConstantInt(location.getHandle().getPrev());
                    if (sslOn != null && sslOn == 1) {
                        sslConnMap.put(location, invoke.getClassName(cpg)+hostName);
                    }
                }
                if (INSECURE_APIS.contains(invoke.getClassName(cpg)) &&
                        "setSSLCheckServerIdentity".equals(invoke.getMethodName(cpg))) {
                    Integer checkOn = ByteCode.getConstantInt(location.getHandle().getPrev());
                    if (checkOn != null && checkOn == 1) {
                        sslCertVerSet.add(invoke.getClassName(cpg)+hostName);
                    }
                }
            }
        }

        //Both conditions - setSSLOnConnect and setSSLCheckServerIdentity
        //haven't been found in the same instance (verifing instance by class name + host name)
        sslConnMap.values().removeAll(sslCertVerSet);

        if (!sslConnMap.isEmpty()) {
            for (Location key : sslConnMap.keySet()) {
                JavaClass clz = classContext.getJavaClass();
                bugReporter.reportBug(new BugInstance(this, INSECURE_SMTP_SSL, Priorities.HIGH_PRIORITY)
                        .addClass(clz)
                        .addMethod(clz, m)
                        .addSourceLine(classContext, m, key));
            }
        }
    }

    @Override
    public void report() {
    }

}
