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
package com.h3xstream.findsecbugs.password;

import com.h3xstream.findsecbugs.common.TaintUtil;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.bcp.FieldVariable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds hardcoded passwords with the Jsch library (SSH client)
 */
public class JschPasswordDetector extends BasicInjectionDetector {

    private static final String HARD_CODE_PASSWORD_TYPE = "HARD_CODE_PASSWORD";

    public JschPasswordDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {
        Taint stringValue = fact.getStackValue(offset);

        if (TaintUtil.isConstantValue(stringValue)) { //Is a constant value
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
                                               InstructionHandle handle) {
        assert invoke != null && cpg != null;

        String className = invoke.getClassName(cpg);
        String method = invoke.getMethodName(cpg);
        String sig    = invoke.getSignature(cpg);
        if(className.equals("com.jcraft.jsch.JSch") &&
            method.equals("addIdentity")) {

            if(sig.equals("(Ljava/lang/String;Ljava/lang/String;)V") ||
                    sig.equals("(Ljava/lang/String;[B)V") ||
                    sig.equals("(Ljava/lang/String;Ljava/lang/String;[B)V") ||
                    sig.equals("(Ljava/lang/String;[B[B[B)V")){
                return new InjectionPoint(new int[]{0}, HARD_CODE_PASSWORD_TYPE);
            }

        }
        return InjectionPoint.NONE;
    }
}
