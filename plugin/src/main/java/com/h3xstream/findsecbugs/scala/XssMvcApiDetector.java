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
package com.h3xstream.findsecbugs.scala;

import com.h3xstream.findsecbugs.injection.AbstractTaintDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.util.ClassName;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

public class XssMvcApiDetector extends AbstractTaintDetector {

    private static final String SCALA_XSS_MVC_API_TYPE = "SCALA_XSS_MVC_API";

    private final String VULNERABLE_CONTENT_TYPE = "text/html";
    private final String INJECTION_SINK = "play/api/mvc/Result.as(Ljava/lang/String;)Lplay/api/mvc/Result;";

    public XssMvcApiDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected void analyzeLocation(ClassContext classContext, Method method, InstructionHandle handle,
                                   ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame fact, String currentMethod)
            throws DataflowAnalysisException {

        SourceLineAnnotation sourceLine = SourceLineAnnotation.fromVisitedInstruction(classContext, method, handle);

        String className = getInstanceClassName(cpg, invoke, fact);
        String methodName = "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);
        String fullMethodName = className.concat(methodName);

        if (fullMethodName.equals(INJECTION_SINK)) {
            // Get the value of the MVC Object
            Taint mvcResultTaint = fact.getStackValue(1);

            // The MVC Result object was tainted - This could still be safe if the content-type is a safe one
            if (!mvcResultTaint.isSafe()) {

                // Get the value of the content-type parameter
                Taint parameterTaint = fact.getStackValue(0);

                if (!parameterTaint.isSafe()
                        || parameterTaint.getConstantValue().equals(VULNERABLE_CONTENT_TYPE)) {

                    bugReporter.reportBug(new BugInstance(this, SCALA_XSS_MVC_API_TYPE, Priorities.NORMAL_PRIORITY)
                            .addClassAndMethod(classContext.getJavaClass(), method).addSourceLine(sourceLine));
                }
            }
        }
    }

    private static String getInstanceClassName(ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame frame) {
        try {
            int instanceIndex = frame.getNumArgumentsIncludingObjectInstance(invoke, cpg) - 1;
            if (instanceIndex != -1) {
                assert instanceIndex < frame.getStackDepth();
                Taint instanceTaint = frame.getStackValue(instanceIndex);
                String className = instanceTaint.getRealInstanceClassName();
                if (className != null) {
                    return className;
                }
            }
        } catch (DataflowAnalysisException ex) {
            assert false : ex.getMessage();
        }
        String dottedClassName = invoke.getReferenceType(cpg).toString();
        return ClassName.toSlashedClassName(dottedClassName);
    }
}