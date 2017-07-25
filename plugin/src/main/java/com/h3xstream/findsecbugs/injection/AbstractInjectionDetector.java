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
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.util.ClassName;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    
    protected final Map<String, Set<InjectionSink>> injectionSinks = new HashMap<String, Set<InjectionSink>>();
    private final Map<MethodAndSink, Taint> sinkTaints = new HashMap<MethodAndSink, Taint>();
    
    protected AbstractInjectionDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    /**
     * Once the analysis is completed, all the collected sinks are reported as bugs.
     */
    @Override
    public void report() {
        // collect sinks and report each once
        Set<InjectionSink> injectionSinksToReport = new HashSet<InjectionSink>();
        for (Set<InjectionSink> injectionSinkSet : injectionSinks.values()) {
            for (InjectionSink injectionSink : injectionSinkSet) {
                injectionSinksToReport.add(injectionSink);
            }
        }
        for (InjectionSink injectionSink : injectionSinksToReport) {
            bugReporter.reportBug(injectionSink.generateBugInstance(false));
        }
    }
    
    @Override
    protected void analyzeLocation(ClassContext classContext, Method method, InstructionHandle handle,
            ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame fact, String currentMethod)
            throws DataflowAnalysisException {
        SourceLineAnnotation sourceLine = SourceLineAnnotation.fromVisitedInstruction(classContext, method, handle);
        checkSink(cpg, invoke, fact, sourceLine, currentMethod);
        InjectionPoint injectionPoint = getInjectionPoint(invoke, cpg, handle);
        for (int offset : injectionPoint.getInjectableArguments()) {
            int priority = getPriorityFromTaintFrame(fact, offset);
            if (priority == Priorities.IGNORE_PRIORITY) {
                continue;
            }

            Taint parameterTaint = fact.getStackValue(offset);

            InjectionSink injectionSink = new InjectionSink(this, injectionPoint.getBugType(), priority,
                    classContext, method, handle, injectionPoint.getInjectableMethod());
            injectionSink.addLines(parameterTaint.getAllLocations());
            injectionSink.addUnknownSources(parameterTaint.getUnknownLocations());
            if (parameterTaint.hasParameters()) {
                // add sink to multi map
                Set<InjectionSink> sinkSet = injectionSinks.get(currentMethod);
                if (sinkSet == null) {
                    sinkSet = new HashSet<InjectionSink>();
                }
                assert !sinkSet.contains(injectionSink) : "duplicate sink";
                sinkSet.add(injectionSink);
                injectionSinks.put(currentMethod, sinkSet);
                sinkTaints.put(new MethodAndSink(currentMethod, injectionSink), parameterTaint);
            } else {
                // sink cannot be influenced by other methods calls, so report it immediately
                bugReporter.reportBug(injectionSink.generateBugInstance(true));
            }
            return;
        }
    }

    /**
     * The default implementation of <code>getPriorityFromTaintFrame()</code> can be overridden if the detector must base its
     * priority on multiple parameters or special conditions like constant values.
     *
     * By default, this method will call the <code>getPriority()</code> method with the parameter taint at the specified offset.
     *
     * @param fact The TaintFrame for the inspected instruction call.
     * @param offset The offset of the checked parameter.
     * @return Priorities interface values from 1 to 5 (Enum-like interface)
     * @throws DataflowAnalysisException An exception thrown when the TaintFrame cannot be analyzed.
     */
    protected int getPriorityFromTaintFrame(TaintFrame fact, int offset)
            throws DataflowAnalysisException {
        Taint parameterTaint = fact.getStackValue(offset);
        return getPriority(parameterTaint);
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
    
    private void checkSink(ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame fact,
            SourceLineAnnotation line, String currentMethod) throws DataflowAnalysisException {
        for (MethodAndSink methodAndSink : getSinks(cpg, invoke, fact)) {
            Taint sinkTaint = sinkTaints.get(methodAndSink);
            assert sinkTaint != null : "sink taint not stored in advance";
            Set<Integer> taintParameters = sinkTaint.getParameters();
            Taint finalTaint = Taint.valueOf(sinkTaint.getNonParametricState());
            for (Integer offset : taintParameters) {
                Taint parameterTaint = fact.getStackValue(offset);
                finalTaint = Taint.merge(finalTaint, parameterTaint);
            }
            if (finalTaint == null) {
                continue;
            }
            if (!sinkTaint.isSafe() && sinkTaint.hasTags()) {
                for (Taint.Tag tag : sinkTaint.getTags()) {
                    finalTaint.addTag(tag);
                }
            }
            if (sinkTaint.isRemovingTags()) {
                for (Taint.Tag tag : sinkTaint.getTagsToRemove()) {
                    finalTaint.removeTag(tag);
                }
            }
            InjectionSink sink = methodAndSink.getSink();
            if (finalTaint.hasParameters()) {
                Set<InjectionSink> sinkSet = injectionSinks.get(currentMethod);
                if (sinkSet == null) {
                    sinkSet = new HashSet<InjectionSink>();
                }
                sinkSet.add(sink);
                injectionSinks.put(currentMethod, sinkSet);
                sinkTaints.put(new MethodAndSink(currentMethod, sink), finalTaint);
            } else {
                // confirm sink to be tainted or called only with safe values
                sink.updateSinkPriority(getPriority(finalTaint));
            }
            if (!finalTaint.isSafe()) {
                sink.addLine(line);
                sink.addLines(finalTaint.getAllLocations());
            }
        }
    }

    private Set<MethodAndSink> getSinks(ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame frame) {
        String className = getInstanceClassName(cpg, invoke, frame);
        String methodName = "." + invoke.getMethodName(cpg) + invoke.getSignature(cpg);
        String fullMethodName = className.concat(methodName);
        Set<InjectionSink> sinks = injectionSinks.get(fullMethodName);
        if (sinks != null) {
            assert !sinks.isEmpty() : "empty set of sinks";
            return getMethodAndSinks(fullMethodName, sinks);
        }
        try {
            if (className.endsWith("]")) {
                // not a real class
                return Collections.emptySet();
            }
            JavaClass javaClass = Repository.lookupClass(className);
            assert javaClass != null;
            return getSuperSinks(javaClass, methodName);
        } catch (ClassNotFoundException ex) {
            AnalysisContext.reportMissingClass(ex);
        }
        return Collections.emptySet();
    }
    
    private Set<MethodAndSink> getMethodAndSinks(String method, Set<InjectionSink> sinks) {
        Set<MethodAndSink> methodAndSinks = new HashSet<MethodAndSink>();
        for (InjectionSink sink : sinks) {
            methodAndSinks.add(new MethodAndSink(method, sink));
        }
        return methodAndSinks;
    }
    
    private Set<MethodAndSink> getSuperSinks(JavaClass javaClass, String method) throws ClassNotFoundException {
        for (JavaClass superClass : javaClass.getSuperClasses()) {
            String fullMethodName = superClass.getClassName().replace('.', '/').concat(method);
            Set<InjectionSink> sinks = injectionSinks.get(fullMethodName);
            if (sinks != null) {
                return getMethodAndSinks(fullMethodName, sinks);
            }
        }
        for (JavaClass interfaceClass : javaClass.getAllInterfaces()) {
            String fullMethodName = interfaceClass.getClassName().replace('.', '/').concat(method);
            Set<InjectionSink> sinks = injectionSinks.get(fullMethodName);
            if (sinks != null) {
                return getMethodAndSinks(fullMethodName, sinks);
            }
        }
        return Collections.emptySet();
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

    abstract protected InjectionPoint getInjectionPoint(
            InvokeInstruction invoke, ConstantPoolGen cpg, InstructionHandle handle);
}
