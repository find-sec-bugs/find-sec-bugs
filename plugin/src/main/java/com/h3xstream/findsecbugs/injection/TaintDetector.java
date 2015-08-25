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
package com.h3xstream.findsecbugs.injection;

import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintDataflow;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.bcel.BCELUtil;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.Global;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.MethodGen;

/**
 * Detector designed for extension to detect injection vulnerabilities
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public abstract class TaintDetector implements Detector {

    private final BugReporter bugReporter;

    protected TaintDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        List<InjectionSource> selectedSources = new ArrayList<InjectionSource>();
        for (InjectionSource source : getInjectionSource()) {
            if (source.isCandidate(cpg)) {
                selectedSources.add(source);
            }
        }
        if (selectedSources.isEmpty()) {
            return;
        }
        JavaClass javaClass = classContext.getJavaClass();
        Method[] methodList = javaClass.getMethods();
        for (Method method : methodList) {
            MethodGen methodGen = classContext.getMethodGen(method);
            if (methodGen == null) {
                continue;
            }
            try {
                analyzeMethod(classContext, method, selectedSources);
            } catch (CheckedAnalysisException e) {
                logException(classContext, method, e);
            } catch (RuntimeException e) {
                logException(classContext, method, e);
            }
        }
    }

    private void analyzeMethod(ClassContext classContext, Method method, List<InjectionSource> selectedSources)
            throws DataflowAnalysisException, CFGBuilderException, CheckedAnalysisException {
        JavaClass javaClass = classContext.getJavaClass();
        TaintDataflow dataflow = getTaintDataFlow(javaClass, method);
        CFG cfg = classContext.getCFG(method);
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext();) {
            Location location = i.next();
            InstructionHandle handle = location.getHandle();
            Instruction instruction = handle.getInstruction();
            if (!(instruction instanceof InvokeInstruction)) {
                continue;
            }
            InvokeInstruction invoke = (InvokeInstruction) instruction;
            InjectionPoint injectionPoint = null;
            for (InjectionSource source : selectedSources) {
                injectionPoint = source.getInjectableParameters(invoke, cpg, handle);
                if (injectionPoint != InjectionPoint.NONE) {
                    break;
                }
            }
            if (injectionPoint == null || injectionPoint == InjectionPoint.NONE) {
                continue;
            }
            TaintFrame fact = dataflow.getFactAtLocation(location);
            if (!fact.isValid()) {
                continue;
            }
            for (int offset : injectionPoint.getInjectableArguments()) {
                Taint parameterTaint = fact.getStackValue(offset);
                reportBug(injectionPoint, classContext, method, location, parameterTaint);
            }
        }
    }

    private void reportBug(InjectionPoint injectionPoint, ClassContext classContext,
            Method method, Location location, Taint taint) {

        //Priority based on tainted
        int priority = getPriority(taint);

        BugInstance bugInstance = new BugInstance(this, injectionPoint.getBugType(),priority);

        //Additional info attached to the bug instance
        JavaClass javaClass = classContext.getJavaClass();
        bugInstance.addClass(javaClass).addMethod(javaClass, method);
        bugInstance.addSourceLine(classContext, method, location);

        //Sink info
        if (injectionPoint.getInjectableMethod()!= null) {
            bugInstance.addString(injectionPoint.getInjectableMethod());
        }
        //Source info
        if (taint.hasTaintedLocations()) {
            addSourceLines(classContext, method, taint.getTaintedLocations(), bugInstance);
        } else {
            addSourceLines(classContext, method, taint.getPossibleTaintedLocations(), bugInstance);
        }
        bugReporter.reportBug(bugInstance);
    }

    private int getPriority(Taint taint) {
        if (taint.isTainted()) {
            //The chain from tainted variable to sink is confirm. (The vulnerability is confirm.)
            return Priorities.HIGH_PRIORITY;
        } else if (!taint.isSafe()) {
            //The taint analysis could NOT confirm that the input come from safe source. (Vulnerable code but not confirm. A review is needed)
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.LOW_PRIORITY;
        }
    }
    
    private void addSourceLines(ClassContext classContext, Method method,
            Collection<Location> locations, BugInstance bugInstance) {
        for (Location location : locations) {
            SourceLineAnnotation taintSource = SourceLineAnnotation
                    .fromVisitedInstruction(classContext, method, location);
            bugInstance.addSourceLine(taintSource);
        }
    }
    
    private TaintDataflow getTaintDataFlow(JavaClass javaClass, Method method)
            throws CheckedAnalysisException {
        MethodDescriptor descriptor = BCELUtil.getMethodDescriptor(javaClass, method);
        return Global.getAnalysisCache().getMethodAnalysis(TaintDataflow.class, descriptor);
    }
    
    private void logException(ClassContext classContext, Method method, Exception ex) {
        bugReporter.logError("Exception while analyzing "
                + classContext.getFullyQualifiedMethodName(method), ex);
    }
    
    @Override
    public void report() {
    }

    public abstract InjectionSource[] getInjectionSource();
}
