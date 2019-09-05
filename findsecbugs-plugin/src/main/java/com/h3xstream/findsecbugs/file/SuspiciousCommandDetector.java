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
package com.h3xstream.findsecbugs.file;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

public class SuspiciousCommandDetector  extends BasicInjectionDetector {

    private static final InvokeMatcherBuilder RUNTIME_EXEC = invokeInstruction().atClass("java.lang.Runtime").atMethod("exec").withArgs("(Ljava/lang/String;)Ljava/lang/Process;");
    private static final InvokeMatcherBuilder RUNTIME_EXEC_WITH_ENV = invokeInstruction().atClass("java.lang.Runtime").atMethod("exec").withArgs("(Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/Process;");

    public SuspiciousCommandDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {

        for(int i=0;i<fact.getStackDepth();i++) {
            Taint valueTaint = fact.getStackValue(i);

            if (valueTaint.getConstantValue() == null) {
                continue;
            }

            String commandExecuted = valueTaint.getConstantValue();

            if (commandExecuted.contains("chmod") && commandExecuted.contains("777")) { //chmod 777 ..
                return Priorities.NORMAL_PRIORITY;
            }
        }
        return Priorities.IGNORE_PRIORITY;


    }

    @Override
    protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
            InstructionHandle handle) {
        assert invoke != null && cpg != null;

        if (RUNTIME_EXEC.matches(invoke, cpg)) {
            return new InjectionPoint(new int[]{0}, OverlyPermissiveFilePermissionDetector.OVERLY_PERMISSIVE_FILE_PERMISSION);
        }
        if (RUNTIME_EXEC_WITH_ENV.matches(invoke, cpg)) {
            return new InjectionPoint(new int[]{1}, OverlyPermissiveFilePermissionDetector.OVERLY_PERMISSIVE_FILE_PERMISSION);
        }
        return InjectionPoint.NONE;
    }
}
