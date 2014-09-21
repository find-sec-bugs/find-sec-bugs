/**
 * Find Security Bugs
 * Copyright (c) 2014, Philippe Arteau, All rights reserved.
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

/**
 * <p>
 *  The main goal of the this detector is to find encryption being done with static initialization vector (IV).
 *  By design, the IV should be change for every message encrypt by a system.
 * </p>
 * <h3>Note on the implementation</h3>
 * <p>
 *  The strategy to find those occurrences is not to backtrack to find the potential source of the bytes being passed.
 *  It will not be trigger if SecureRandom instance is use. Therefor, it is very likely to trigger false positive if the
 *  encryption is separate from the IV generation.
 * </p>
 */
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

        boolean foundSafeIvGeneration = false;

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = nextLocation(i, cpg);
            Instruction inst = location.getHandle().getInstruction();

            if (inst instanceof INVOKEVIRTUAL) {
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                if (("java.security.SecureRandom").equals(invoke.getClassName(cpg)) &&
                        "nextBytes".equals(invoke.getMethodName(cpg))) {
                    foundSafeIvGeneration = true;
                }
            }

            if (!foundSafeIvGeneration && inst instanceof INVOKESPECIAL) { //MessageDigest.digest is called
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
