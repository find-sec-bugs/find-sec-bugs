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
package com.h3xstream.findsecbugs.cookie;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;

import java.util.Iterator;
import org.apache.bcel.generic.InstructionHandle;

public class CookieFlagsDetector implements Detector {

    private static final String INSECURE_COOKIE_TYPE = "INSECURE_COOKIE";
    private static final String HTTPONLY_COOKIE_TYPE = "HTTPONLY_COOKIE";

    private BugReporter bugReporter;

    private static final int TRUE_INT_VALUE = 1;

    public CookieFlagsDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {

            try {
                analyzeMethod(m,classContext);
            } catch (Exception e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws Exception {
        //System.out.println("==="+m.getName()+"===");

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location loc = i.next();
            //ByteCode.printOpCode(loc.getHandle().getInstruction(), cpg);

            Instruction inst = loc.getHandle().getInstruction();
            if(inst instanceof INVOKESPECIAL) {
                INVOKESPECIAL invoke = (INVOKESPECIAL) inst;
                if ("javax.servlet.http.Cookie".equals(invoke.getClassName(cpg)) &&
                        "<init>".equals(invoke.getMethodName(cpg))){

                    Location setSecureLocation = getSetSecureLocation(cpg, loc);
                    if (setSecureLocation == null) {

                        JavaClass javaClass = classContext.getJavaClass();

                        bugReporter.reportBug(new BugInstance(this, INSECURE_COOKIE_TYPE, Priorities.NORMAL_PRIORITY) //
                                .addClass(javaClass)
                                .addMethod(javaClass, m)
                                .addSourceLine(classContext, m, loc));
                    }

                    Location setHttpOnlyLocation = getSetHttpOnlyLocation(cpg, loc);
                    if (setHttpOnlyLocation == null) {

                        JavaClass javaClass = classContext.getJavaClass();

                        bugReporter.reportBug(new BugInstance(this, HTTPONLY_COOKIE_TYPE, Priorities.NORMAL_PRIORITY) //
                                .addClass(javaClass)
                                .addMethod(javaClass, m)
                                .addSourceLine(classContext, m, loc));
                    }
                }
            }
        }
    }

    private Location getSetSecureLocation(ConstantPoolGen cpg, Location startLocation) {
        Location location = startLocation;
        InstructionHandle handle = location.getHandle();

        while (handle.getNext() != null) {
            handle = handle.getNext();
            Instruction nextInst = handle.getInstruction();

            if(nextInst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) nextInst;
                if ("javax.servlet.http.Cookie".equals(invoke.getClassName(cpg)) &&
                        "setSecure".equals(invoke.getMethodName(cpg))) {
                    Integer val = ByteCode.getConstantInt(handle.getPrev());
                    //if(val != null) System.out.println("setSecure -> "+val.intValue());
                    if (val != null && val == TRUE_INT_VALUE) {
                        return new Location(handle, location.getBasicBlock());
                    }
                }
            }
        }

        return null;
    }

    private Location getSetHttpOnlyLocation(ConstantPoolGen cpg, Location startLocation) {
        Location location = startLocation;
        InstructionHandle handle = location.getHandle();

        while (handle.getNext() != null) {
            handle = handle.getNext();
            Instruction nextInst = handle.getInstruction();

            if(nextInst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) nextInst;
                if ("javax.servlet.http.Cookie".equals(invoke.getClassName(cpg)) &&
                        "setHttpOnly".equals(invoke.getMethodName(cpg))) {
                    Integer val = ByteCode.getConstantInt(handle.getPrev());
                    //if(val != null) System.out.println("setSecure -> "+val.intValue());
                    if (val != null && val == TRUE_INT_VALUE) {
                        return new Location(handle, location.getBasicBlock());
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void report() {

    }
}