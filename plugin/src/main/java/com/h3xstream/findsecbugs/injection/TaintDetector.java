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
import com.h3xstream.findsecbugs.taintanalysis.TaintLocation;
import com.h3xstream.findsecbugs.taintanalysis.TaintSink;
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
import edu.umd.cs.findbugs.util.ClassName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final Map<String, Set<TaintSink>> methodsWithSinks = new HashMap<String, Set<TaintSink>>();

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
            //return; // analysis still must be requested
        }
        for (Method method : classContext.getMethodsInCallOrder()) {
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
        String currentMethod = getFullMethodName(classContext.getMethodGen(method));
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext();) {
            Location location = i.next();
            InstructionHandle handle = location.getHandle();
            Instruction instruction = handle.getInstruction();
            if (!(instruction instanceof InvokeInstruction)) {
                continue;
            }
            InvokeInstruction invoke = (InvokeInstruction) instruction;
            TaintFrame fact = dataflow.getFactAtLocation(location);
            if (!fact.isValid()) {
                continue;
            }
            
            String calledMethod = getFullMethodName(cpg, invoke);
            if (methodsWithSinks.containsKey(calledMethod)) {
                Set<TaintSink> sinks = methodsWithSinks.get(calledMethod);
                for (TaintSink sink : sinks) {
                    Taint sinkTaint = sink.getTaint();
                    Set<Integer> taintParameters = sinkTaint.getTaintParameters();
                    Taint finalTaint = sinkTaint.getNonParametricTaint();
                    for (Integer offset : taintParameters) {
                        Taint parameterTaint = fact.getStackValue(offset);
                        finalTaint = Taint.merge(finalTaint, parameterTaint);
                    }
                    if (finalTaint == null) {
                        continue;
                    }
                    if (finalTaint.isTainted()) {
                        BugInstance bugInstance = sink.getBugInstance();
                        bugInstance.setPriority(Priorities.HIGH_PRIORITY);
                        bugInstance.addSourceLine(classContext, method, handle);
                    } else if (finalTaint.hasTaintParameters()) {
                        BugInstance bugInstance = sink.getBugInstance();
                        bugInstance.addSourceLine(classContext, method, handle);
                        delayBugToReport(currentMethod, finalTaint, bugInstance);
                    }
                }
            }
            
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
            
            for (int offset : injectionPoint.getInjectableArguments()) {
                Taint parameterTaint = fact.getStackValue(offset);
                reportBug(injectionPoint, classContext, method, location, parameterTaint, currentMethod);
            }
        }
    }

    private void reportBug(InjectionPoint injectionPoint, ClassContext classContext,
            Method method, Location location, Taint taint, String currentMethod) {
        BugInstance bugInstance = getBugInstance(injectionPoint, taint);
        JavaClass javaClass = classContext.getJavaClass();
        bugInstance.addClass(javaClass).addMethod(javaClass, method);
        bugInstance.addSourceLine(classContext, method, location);
        if (injectionPoint.getInjectableMethod()!= null) {
            bugInstance.addString(injectionPoint.getInjectableMethod());
        }
        if (taint.hasTaintedLocations()) {
            addSourceLines(taint.getTaintedLocations(), bugInstance);
        } else {
            addSourceLines(taint.getPossibleTaintedLocations(), bugInstance);
        }
        if (bugInstance.getPriority() == Priorities.NORMAL_PRIORITY && taint.hasTaintParameters()) {
            delayBugToReport(currentMethod, taint, bugInstance);
        } else {
            bugReporter.reportBug(bugInstance);
        }
    }

    private void delayBugToReport(String method, Taint taint, BugInstance bug) {
        TaintSink taintSink = new TaintSink(taint, bug);
        Set<TaintSink> sinkSet = methodsWithSinks.getOrDefault(method, new HashSet<TaintSink>());
        sinkSet.add(taintSink);
        methodsWithSinks.put(method, sinkSet);
    }
    
    private BugInstance getBugInstance(InjectionPoint injectionPoint, Taint taint) {
        int priority;
        if (taint.isTainted()) {
            priority = Priorities.HIGH_PRIORITY;
        } else if (!taint.isSafe()) {
            priority = Priorities.NORMAL_PRIORITY;
        } else {
            priority = Priorities.LOW_PRIORITY;
        }
        return new BugInstance(this, injectionPoint.getBugType(), priority);
    }
    
    private void addSourceLines(Collection<TaintLocation> locations, BugInstance bugInstance) {
        List<SourceLineAnnotation> annotations = new LinkedList<SourceLineAnnotation>();
        for (TaintLocation location : locations) {
            SourceLineAnnotation taintSource = SourceLineAnnotation.fromVisitedInstruction(
                    location.getMethodDescriptor(), location.getPosition());
            annotations.add(taintSource);
        }
        Collections.sort(annotations);
        SourceLineAnnotation annotation = null;
        for (Iterator<SourceLineAnnotation> it = annotations.iterator(); it.hasNext();) {
            SourceLineAnnotation prev = annotation;
            annotation = it.next();
            if (prev != null && prev.getClassName().equals(annotation.getClassName())
                    && prev.getStartLine() == annotation.getStartLine()) {
                // keep only one annotation per line
                it.remove();
            }
        }
        for (SourceLineAnnotation sourceLine : annotations) {
            bugInstance.addSourceLine(sourceLine);
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
    
    private String getFullMethodName(ConstantPoolGen cpg, InvokeInstruction invoke) {
        String dottedClassName = invoke.getReferenceType(cpg).toString();
        StringBuilder builder = new StringBuilder(ClassName.toSlashedClassName(dottedClassName));
        builder.append(".").append(invoke.getMethodName(cpg)).append(invoke.getSignature(cpg));
        return builder.toString();
    }
    
    private String getFullMethodName(MethodGen methodGen) {
        String methodNameWithSignature = methodGen.getName() + methodGen.getSignature();
        String slashedClassName = methodGen.getClassName().replace('.', '/');
        return slashedClassName + "." + methodNameWithSignature;
    }
    
    @Override
    public void report() {
        Set<BugInstance> bugs = new HashSet<BugInstance>();
        for (Set<TaintSink> sinkSet : methodsWithSinks.values()) {
            for (TaintSink sink : sinkSet) {
                // set removes duplicit bug instances
                bugs.add(sink.getBugInstance());
            }
        }
        for (BugInstance bug : bugs) {
            bugReporter.reportBug(bug);
        }
    }

    public abstract InjectionSource[] getInjectionSource();
}
