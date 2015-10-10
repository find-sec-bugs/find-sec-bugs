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
import edu.umd.cs.findbugs.ba.AnalysisContext;
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
import java.util.Arrays;
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
        for (Method method : classContext.getMethodsInCallOrder()) {
            MethodGen methodGen = classContext.getMethodGen(method);
            if (methodGen == null) {
                continue;
            }
            try {
                analyzeMethod(classContext, method, Arrays.asList(getInjectionSource()));
            } catch (CheckedAnalysisException e) {
                logException(classContext, method, e);
            } catch (RuntimeException e) {
                logException(classContext, method, e);
            }
        }
    }

    private void analyzeMethod(ClassContext classContext, Method method, Collection<InjectionSource> selectedSources)
            throws DataflowAnalysisException, CheckedAnalysisException {
        TaintDataflow dataflow = getTaintDataFlow(classContext, method);
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        String currentMethod = getFullMethodName(classContext.getMethodGen(method));
        for (Iterator<Location> i = getLocationIterator(classContext, method); i.hasNext();) {
            Location location = i.next();
            InstructionHandle handle = location.getHandle();
            Instruction instruction = handle.getInstruction();
            if (!(instruction instanceof InvokeInstruction)) {
                continue;
            }
            InvokeInstruction invoke = (InvokeInstruction) instruction;
            TaintFrame fact = dataflow.getFactAtLocation(location);
            assert fact != null;
            if (!fact.isValid()) {
                continue;
            }
            SourceLineAnnotation sourceLine = SourceLineAnnotation
                    .fromVisitedInstruction(classContext, method, handle);
            checkTaintSink(cpg, invoke, fact, sourceLine, currentMethod);
            InjectionPoint injectionPoint = getInjectionPoint(invoke, cpg, handle, selectedSources);
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
            }
        }
    }

    private static Iterator<Location> getLocationIterator(ClassContext classContext, Method method)
            throws CheckedAnalysisException {
        try {
            return classContext.getCFG(method).locationIterator();
        } catch (CFGBuilderException ex) {
            throw new CheckedAnalysisException("cannot get control flow graph", ex);
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
                    bugInstance.setPriority(Priorities.HIGH_PRIORITY);
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

    private static InjectionPoint getInjectionPoint(InvokeInstruction invoke, ConstantPoolGen cpg,
            InstructionHandle handle, Collection<InjectionSource> sources) {
        InjectionPoint injectionPoint = null;
        for (InjectionSource source : sources) {
            injectionPoint = source.getInjectableParameters(invoke, cpg, handle);
            if (injectionPoint != InjectionPoint.NONE) {
                break;
            }
        }
        if (injectionPoint == null) {
            injectionPoint = InjectionPoint.NONE;
        }
        return injectionPoint;
    }

    private void reportBug(BugInstance bugInstance, Taint taint, String currentMethod) {
        addSourceLines(taint.getLocations(), bugInstance);
        if (bugInstance.getPriority() == Priorities.NORMAL_PRIORITY && taint.hasParameters()) {
            delayBugToReport(currentMethod, taint, bugInstance);
        } else {
            bugReporter.reportBug(bugInstance);
        }
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

    private int getPriority(Taint taint) {
        if (taint.isTainted()) {
            return Priorities.HIGH_PRIORITY;
        } else if (!taint.isSafe()) {
            return Priorities.NORMAL_PRIORITY;
        } else {
            return Priorities.IGNORE_PRIORITY;
        }
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

    private static TaintDataflow getTaintDataFlow(ClassContext classContext, Method method)
            throws CheckedAnalysisException {
        MethodDescriptor descriptor = BCELUtil.getMethodDescriptor(classContext.getJavaClass(), method);
        return Global.getAnalysisCache().getMethodAnalysis(TaintDataflow.class, descriptor);
    }

    private void logException(ClassContext classContext, Method method, Exception ex) {
        bugReporter.logError("Exception while analyzing "
                + classContext.getFullyQualifiedMethodName(method), ex);
    }

    private static String getFullMethodName(MethodGen methodGen) {
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
