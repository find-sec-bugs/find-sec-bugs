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
import edu.umd.cs.findbugs.Priorities;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;

import java.util.Iterator;

public class BadHexadecimalConversionDetector implements Detector {

    private static final String BAD_HEXA_CONVERSION_TYPE = "BAD_HEXA_CONVERSION";
    private BugReporter bugReporter;

    public BadHexadecimalConversionDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {
            MethodGen methodGen = classContext.getMethodGen(m);

            //To suspect that an invalid String representation is being build,
            //we identify the construction of a MessageDigest and
            //the use of a function that trim leading 0.
            boolean invokeMessageDigest = false;
            boolean invokeToHexString = false;

            ConstantPoolGen cpg = classContext.getConstantPoolGen();
            if (methodGen == null || methodGen.getInstructionList() == null) {
                continue; //No instruction .. nothing to do
            }
            for (Iterator itIns = methodGen.getInstructionList().iterator();itIns.hasNext();) {
                Instruction inst = ((InstructionHandle) itIns.next()).getInstruction();
//                    ByteCode.printOpCode(inst, cpg);

                if (inst instanceof INVOKEVIRTUAL) { //MessageDigest.digest is called
                    INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                    if ("java.security.MessageDigest".equals(invoke.getClassName(cpg)) && "digest".equals(invoke.getMethodName(cpg))) {
                        invokeMessageDigest = true;
                    }
                } else if (inst instanceof INVOKESTATIC && invokeMessageDigest) { //The conversion must occurs after the digest was created
                    INVOKESTATIC invoke = (INVOKESTATIC) inst;
                    if ("java.lang.Integer".equals(invoke.getClassName(cpg)) && "toHexString".equals(invoke.getMethodName(cpg))) {
                        invokeToHexString = true;
                    }
                }
            }

            if (invokeMessageDigest && invokeToHexString) {
                bugReporter.reportBug(new BugInstance(this, BAD_HEXA_CONVERSION_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClassAndMethod(javaClass, m));
            }
        }
    }

    @Override
    public void report() {

    }
}
