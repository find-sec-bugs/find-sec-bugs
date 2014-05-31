/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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
import edu.umd.cs.findbugs.ba.*;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StaticIvDetector implements Detector {

    private static final boolean DEBUG = false;
    private static final String STATIC_IV = "STATIC_IV";
    private BugReporter bugReporter;

    public StaticIvDetector(BugReporter bugReporter) {
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
            } catch (DataflowAnalysisException e) {
            }
        }
    }


    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {
        MethodGen methodGen = classContext.getMethodGen(m);

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        Set<Integer> taintedLocals = new HashSet<Integer>();

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = nextLocation(i, cpg);
            Instruction inst = location.getHandle().getInstruction();

            if (inst instanceof INVOKESPECIAL) { //MessageDigest.digest is called
                INVOKESPECIAL invoke = (INVOKESPECIAL) inst;
                if (("javax.crypto.spec.IvParameterSpec").equals(invoke.getClassName(cpg)) &&
                        "<init>".equals(invoke.getMethodName(cpg))) {
                    JavaClass clz = classContext.getJavaClass();
                    bugReporter.reportBug(new BugInstance(this, STATIC_IV, Priorities.NORMAL_PRIORITY) //
                            .addClass(clz)
                            .addMethod(clz,m)
                            .addSourceLine(classContext,m,location));
                }
            }
        }
    }

    private Location nextLocation(Iterator<Location> i,ConstantPoolGen cpg) {
        Location loc = i.next();
        if(DEBUG) ByteCode.printOpCode(loc.getHandle().getInstruction(), cpg);
        return loc;
    }

    @Override
    public void report() {

    }
}
