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
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Location;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
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
            } catch (CFGBuilderException e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException {
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
                        "<init>".equals(invoke.getMethodName(cpg))) {

                    // The following call should push the cookie onto the stack
                    Instruction cookieStoreInstruction = loc.getHandle().getNext().getInstruction();
                    if (cookieStoreInstruction instanceof ASTORE) {

                        // We will use the position of the object on the stack to track the cookie
                        ASTORE storeInstruction = (ASTORE)cookieStoreInstruction;

                        Location setSecureLocation = getSetSecureLocation(cpg, loc, storeInstruction.getIndex());
                        if (setSecureLocation == null) {

                            JavaClass javaClass = classContext.getJavaClass();

                            bugReporter.reportBug(new BugInstance(this, INSECURE_COOKIE_TYPE, Priorities.NORMAL_PRIORITY) //
                                    .addClass(javaClass)
                                    .addMethod(javaClass, m)
                                    .addSourceLine(classContext, m, loc));
                        }

                        Location setHttpOnlyLocation = getSetHttpOnlyLocation(cpg, loc, storeInstruction.getIndex());
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
    }

    /**
     * This method is used to track calls made on a specific object. For instance, this could be used to track if "setHttpOnly(true)"
     * was executed on a specific cookie object.
     *
     * This allows the detector to find interchanged calls like this
     *
     * Cookie cookie1 = new Cookie("f", "foo");     <- This cookie is unsafe
     * Cookie cookie2 = new Cookie("b", "bar");     <- This cookie is safe
     * cookie1.setHttpOnly(false);
     * cookie2.setHttpOnly(true);
     *
     * @param cpg ConstantPoolGen
     * @param startLocation The Location of the cookie initialization call.
     * @param objectStackLocation The index of the cookie on the stack.
     * @param invokeInstruction The instruction we want to detect.s
     * @return The location of the invoke instruction provided for the cookie at a specific index on the stack.
     */
    private Location getCookieInstructionLocation(ConstantPoolGen cpg, Location startLocation, int objectStackLocation, String invokeInstruction) {
        Location location = startLocation;
        InstructionHandle handle = location.getHandle();

        int loadedStackValue = 0;

        // Loop until we find the setSecure call for this cookie
        while (handle.getNext() != null) {
            handle = handle.getNext();
            Instruction nextInst = handle.getInstruction();

            // We check if the index of the cookie used for this invoke is the same as the one provided
            if (nextInst instanceof ALOAD) {
                ALOAD loadInst = (ALOAD)nextInst;
                loadedStackValue = loadInst.getIndex();
            }

            if (nextInst instanceof INVOKEVIRTUAL
                    && loadedStackValue == objectStackLocation) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) nextInst;

                String methodNameWithSignature = invoke.getClassName(cpg) + "." + invoke.getMethodName(cpg);

                if (methodNameWithSignature.equals(invokeInstruction)) {

                    Integer val = ByteCode.getConstantInt(handle.getPrev());

                    if (val != null && val == TRUE_INT_VALUE) {
                        return new Location(handle, location.getBasicBlock());
                    }
                }
            }
        }

        return null;
    }

    private Location getSetSecureLocation(ConstantPoolGen cpg, Location startLocation, int stackLocation) {
        return getCookieInstructionLocation(cpg, startLocation, stackLocation, "javax.servlet.http.Cookie.setSecure");
    }

    private Location getSetHttpOnlyLocation(ConstantPoolGen cpg, Location startLocation, int stackLocation) {
        return getCookieInstructionLocation(cpg, startLocation, stackLocation, "javax.servlet.http.Cookie.setHttpOnly");
    }

    @Override
    public void report() {

    }
}