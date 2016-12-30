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
package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.Instruction;
import java.util.Iterator;

public class PermissiveCORSDetector implements Detector {

    private static final String PERMISSIVE_CORS = "PERMISSIVE_CORS";

    private BugReporter bugReporter;

    public PermissiveCORSDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }
    
    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {

            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);
        
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();

            Instruction inst = location.getHandle().getInstruction();

            if (inst instanceof INVOKEINTERFACE) {
                INVOKEINTERFACE invoke = (INVOKEINTERFACE) inst;
                String methodName = invoke.getMethodName(cpg);
                String className = invoke.getClassName(cpg);

                if (className.equals("javax.servlet.http.HttpServletResponse") &&
                   (methodName.equals("addHeader") || methodName.equals("setHeader"))) {

                    LDC ldc = ByteCode.getPrevInstruction(location.getHandle().getPrev(), LDC.class);
                    if (ldc != null) {
                        String headerValue = ByteCode.getConstantLDC(location.getHandle().getPrev(), cpg, String.class);
                        if ("Access-Control-Allow-Origin".equalsIgnoreCase((String)ldc.getValue(cpg)) &&
                            (headerValue.contains("*") || "null".equalsIgnoreCase(headerValue))) {

                            JavaClass clz = classContext.getJavaClass();
                            bugReporter.reportBug(new BugInstance(this, PERMISSIVE_CORS, Priorities.HIGH_PRIORITY)
                            .addClass(clz)
                            .addMethod(clz, m)
                            .addSourceLine(classContext, m, location));
                        }
                    }
                }
            }
        }         
        
    }

    @Override
    public void report() {
    }
}