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
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.TaintLocation;
import com.h3xstream.findsecbugs.taintanalysis.TaintSink;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.util.ClassName;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * Detector designed for extension to detect injection vulnerabilities
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public abstract class AbstractInjectionDetector extends AbstractTaintDetector {
    
    protected final Map<String, Set<TaintSink>> methodsWithSinks = new HashMap<String, Set<TaintSink>>();
    
    protected AbstractInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    /**
     * Once the analysis is completed, all the collected sink are report as bug.
     */
    @Override
    public void report() {
        //The HashSet data structure was chosen to remove duplicate bug instances
        Set<BugInstance> bugs = new HashSet<BugInstance>();
        for (Set<TaintSink> sinkSet : methodsWithSinks.values()) {
            for (TaintSink sink : sinkSet) {
                bugs.add(sink.getBugInstance());
            }
        }
        for (BugInstance bug : bugs) {
            bugReporter.reportBug(bug);
        }
    }
    
    @Override
    protected void analyzeLocation(ClassContext classContext, Method method, InstructionHandle handle,
            ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame fact, String currentMethod)
            throws DataflowAnalysisException {
        SourceLineAnnotation sourceLine = SourceLineAnnotation.fromVisitedInstruction(classContext, method, handle);
        checkTaintSink(cpg, invoke, fact, sourceLine, currentMethod);
        InjectionPoint injectionPoint = getInjectionPoint(invoke, cpg, handle);

        for (int offset : injectionPoint.getInjectableArguments()) {
            Taint parameterTaint = fact.getStackValue(offset);
            int priority = getPriority(parameterTaint);
            if (priority == Priorities.IGNORE_PRIORITY) {
                continue;
            }
            BugInstance bugInstance = new BugInstance(this, injectionPoint.getBugType(), priority);
            bugInstance.addClassAndMethod(classContext.getJavaClass(), method);
            bugInstance.addSourceLine(sourceLine);
            if (injectionPoint.getInjectableMethod() != null) {
                bugInstance.addString(injectionPoint.getInjectableMethod());
            }
            reportBug(bugInstance, parameterTaint, currentMethod);
            return;
        }
    }

    /**
     * The default implementation of <code>getPriority()</code> can be overridden if the severity and the confidence for risk
     * is particular.
     *
     * By default, injection will be rated "High" if the complete link between source and sink is made.
     * If it is not the case but concatenation with external source is made, "Medium" is used.
     *
     * @param taint Detail about the state of the value passed (Cumulative information leading to the variable passed).
     * @return Priorities interface values from 1 to 5 (Enum-like interface)
     */
    protected int getPriority(Taint taint) {
        if (taint.isTainted()) {
            return Priorities.HIGH_PRIORITY;
        } else if (!taint.isSafe()) {
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
    }
    
    private void checkTaintSink(ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame fact,
            SourceLineAnnotation sourceLine, String currentMethod) throws DataflowAnalysisException {
        for (TaintSink sink : getSinks(cpg, invoke, fact)) {
            Taint sinkTaint = sink.getTaint();
            Set<Integer> taintParameters = sinkTaint.getParameters();
            Taint finalTaint = Taint.valueOf(sinkTaint.getNonParametricState());
            for (Integer offset : taintParameters) {
                Taint parameterTaint = fact.getStackValue(offset);
                finalTaint = Taint.merge(finalTaint, parameterTaint);
            }
            if (finalTaint == null) {
                continue;
            }
            if (finalTaint.isTainted() || finalTaint.hasParameters()) {
                BugInstance bugInstance = sink.getBugInstance();
                bugInstance.addSourceLine(sourceLine);
                addSourceLines(finalTaint.getLocations(), bugInstance);
                if (finalTaint.isTainted()) {
                    bugInstance.setPriority(getPriority(finalTaint));
                } else {
                    assert finalTaint.isUnknown();
                    delayBugToReport(currentMethod, finalTaint, bugInstance);
                }
            }
        }
    }

    private Set<TaintSink> getSinks(ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame frame) {
        String className = getInstanceClassName(cpg, invoke, frame);
        String methodName = "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);
        Set<TaintSink> sinks = methodsWithSinks.get(className.concat(methodName));
        if (sinks != null) {
            return sinks;
        }
        try {
            JavaClass javaClass = Repository.lookupClass(className);
            assert javaClass != null;
            return getSuperSinks(javaClass, methodName);
        } catch (ClassNotFoundException ex) {
            AnalysisContext.reportMissingClass(ex);
        }
        return Collections.emptySet();
    }
    
    private Set<TaintSink> getSuperSinks(JavaClass javaClass, String method) throws ClassNotFoundException {
        for (JavaClass superClass : javaClass.getSuperClasses()) {
            String fullMethodName = superClass.getClassName().replace('.', '/').concat(method);
            Set<TaintSink> sinks = methodsWithSinks.get(fullMethodName);
            if (sinks != null) {
                return sinks;
            }
        }
        for (JavaClass interfaceClass : javaClass.getAllInterfaces()) {
            String fullMethodName = interfaceClass.getClassName().replace('.', '/').concat(method);
            Set<TaintSink> sinks = methodsWithSinks.get(fullMethodName);
            if (sinks != null) {
                return sinks;
            }
        }
        return Collections.emptySet();
    }
    
    private void reportBug(BugInstance bugInstance, Taint taint, String currentMethod) {
        addSourceLines(taint.getLocations(), bugInstance);
        if (shouldBugBeDelayed(taint, bugInstance)) {
            delayBugToReport(currentMethod, taint, bugInstance);
        } else {
            bugReporter.reportBug(bugInstance);
        }
    }

    private boolean shouldBugBeDelayed(Taint taint, BugInstance bugInstance) {
        if (!taint.hasParameters()) {
            return false;
        }
        return bugInstance.getPriority() == getPriority(new Taint(Taint.State.UNKNOWN));
    }
    
    private void delayBugToReport(String method, Taint taint, BugInstance bug) {
        TaintSink taintSink = new TaintSink(taint, bug);
        Set<TaintSink> sinkSet = methodsWithSinks.get(method);
        if (sinkSet == null) {
            sinkSet = new HashSet<TaintSink>();
        }
        sinkSet.add(taintSink);
        methodsWithSinks.put(method, sinkSet);
    }

    private static void addSourceLines(Collection<TaintLocation> locations, BugInstance bugInstance) {
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
    
    abstract protected InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg, InstructionHandle handle);
}
