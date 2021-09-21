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

package com.h3xstream.findsecbugs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.Set;

import com.h3xstream.findsecbugs.injection.AbstractTaintDetector;
import com.h3xstream.findsecbugs.injection.ClassMethodSignature;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import com.h3xstream.findsecbugs.taintanalysis.TaintFrame;
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSource;
import com.h3xstream.findsecbugs.taintanalysis.data.UnknownSourceType;

import org.apache.bcel.Const;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;

public class PossibleUntrustedObjectInjection extends AbstractTaintDetector {
    private JavaClass privilegedActionClass;

    private static class CalleeInfo {
        JavaClass calledClass;
        Method calledMethod;
        Taint calledOn;
        ClassContext classContext;
        Method callerMethod;
        InstructionHandle handle;
        CalleeInfo(JavaClass cls, Method called, Taint obj, ClassContext cont, Method caller, InstructionHandle hand) {
            calledClass = cls;
            calledMethod = called;
            calledOn = obj;
            classContext = cont;
            callerMethod = caller;
            handle = hand;
        }
    }

    private static class CallerInfo {
        Taint calledOn;
        ClassContext classContext;
        Method callerMethod;
        InstructionHandle handle;
        CallerInfo(Taint obj, ClassContext cont, Method caller, InstructionHandle hand) {
            calledOn = obj;
            classContext = cont;
            callerMethod = caller;
            handle = hand;
        }
    }

    private static class CallPair {
        CalleeInfo outside;
        CallerInfo inside;
        CallPair(CalleeInfo out, CallerInfo in) {
            outside = out;
            inside = in;
        }
    }

    private Map<Method, Set<CalleeInfo>> nonFinalMethodsCalledOnTainted = new HashMap<>();
    private Map<Method, Set<CallerInfo>> methodsCalledInsidePrivilegedAction = new HashMap<>();

    private JavaClass lastClass = null;
    private Method lastMethod = null;

    public PossibleUntrustedObjectInjection(BugReporter bugReporter) {
        super(bugReporter);
        try {
            privilegedActionClass = Repository.lookupClass("java.security.PrivilegedAction");
        } catch (ClassNotFoundException e) {
            AnalysisContext.reportMissingClass(e);
        }
    }

    @Override
    public void analyzeLocation(ClassContext classContext, Method callerMethod, InstructionHandle handle,
            ConstantPoolGen cpg, InvokeInstruction invoke, TaintFrame fact, ClassMethodSignature classMethodSignature)
            throws DataflowAnalysisException {
        JavaClass callerClass = classContext.getJavaClass();

        if (lastClass != null && lastMethod != null && callerClass != lastClass && callerMethod != lastMethod) {
            cleanupMethodsCalledOnTainted(callerMethod);            
        }
        lastClass = callerClass;
        lastMethod = callerMethod;

        if (!(invoke instanceof INVOKEVIRTUAL || invoke instanceof INVOKESTATIC)) {
            return;
        }

        if (callerClass.isNested() && Const.CONSTRUCTOR_NAME.equals(callerMethod.getName())) {
            return;
        }

        try {
            JavaClass calledClass = Repository.lookupClass(invoke.getClassName(cpg));
            Method calledMethod = getMethod(calledClass, invoke.getMethodName(cpg), invoke.getSignature(cpg));
            if (calledMethod == null) { // Scala classes do not contain all their methods in BCEL
                return;
            }

            if (invoke instanceof INVOKEVIRTUAL) {
                Taint object = fact.getStackValue(0);
                if ("run".equals(callerMethod.getName()) && callerClass.instanceOf(privilegedActionClass)) {
                    if (!calledClass.isFinal() && !calledMethod.isFinal()
                            && isNestingMethodLocalVariable(object, callerClass.getClassName())) {
                        addToMethodsCalledInsidePrivilegedAction(calledMethod, object, classContext, callerMethod,
                                handle);
                    }
                } else if (!calledClass.isFinal() && !calledMethod.isFinal() && object.isTainted()) {
                    addToNonFinalMethodsCalledOnTainted(callerMethod, calledClass, calledMethod, object, classContext,
                            handle);
                }
            } else if (invoke instanceof INVOKESTATIC && "doPrivileged".equals(calledMethod.getName())
                    && "java.security.AccessController".equals(calledClass.getClassName())) {
                Taint action = fact.getStackValue(0);
                CallPair callPair = lookForCalledOutsideAndInside(callerMethod, action);
                if (callPair != null) {
                    reportBug(classContext, callerMethod, handle, callPair);
                }
            }
        } catch (ClassNotFoundException e) {
            AnalysisContext.reportMissingClass(e);
        }
    }

    private Method getMethod(JavaClass cls, String methodName, String methodSig) {
        Optional<Method> method = Optional.empty();
        try {
            do {
                method = Arrays.stream(cls.getMethods()).filter(m -> m.getName().equals(methodName)
                        && m.getSignature().equals(methodSig)).findFirst();
                cls = cls.getSuperClass();
            } while (method.isEmpty() && cls != null);
        } catch (ClassNotFoundException e) {
            AnalysisContext.reportMissingClass(e);
        }

        if (!method.isEmpty()) {
            return method.get();
        } else {
            return null;
        }
    }

    private boolean isNestingMethodLocalVariable(Taint object, String className) {
        for (UnknownSource src: object.getSources()) {
            if (src.getSourceType() == UnknownSourceType.FIELD
                    && src.getSignatureField().matches(className.replace('.', '/').replace("$", "\\$")
                            + "\\.val\\$.*")) {
                return true;
            }
        }
        return false;
    }

    private void addToMethodsCalledInsidePrivilegedAction(Method calledMethod, Taint object,
            ClassContext callerClassContext, Method callerMethod, InstructionHandle handle) {
        Set<CallerInfo> objects = methodsCalledInsidePrivilegedAction.get(calledMethod);
        if (objects == null) {
            objects = new HashSet<>();
            methodsCalledInsidePrivilegedAction.put(calledMethod, objects);
        }
        objects.add(new CallerInfo(object, callerClassContext, callerMethod, handle));
    }

    private void addToNonFinalMethodsCalledOnTainted(Method callerMethod, JavaClass calledClass, Method calledMethod,
            Taint object, ClassContext callerClassContext, InstructionHandle handle) {
        Set<CalleeInfo> objects = nonFinalMethodsCalledOnTainted.get(callerMethod);
        if (objects == null) {
            objects = new HashSet<>();
            nonFinalMethodsCalledOnTainted.put(callerMethod, objects);
        }
        objects.add(new CalleeInfo(calledClass, calledMethod, object, callerClassContext, callerMethod, handle));
    }

    private CallPair lookForCalledOutsideAndInside(Method callerMethod, Taint action) {
        Set<CalleeInfo> callees = nonFinalMethodsCalledOnTainted.get(callerMethod);
        if (callees == null) {
            return null;
        }

        for (CalleeInfo calleeInfo: callees) {
            CallerInfo inside = getCalledInside(callerMethod, action, calleeInfo);
            if (inside != null) {
                return new CallPair(calleeInfo, inside);
            }
        }
        return null;
    }

    private CallerInfo getCalledInside(Method callerMethod, Taint action, CalleeInfo calleeInfo) {
        Set<CallerInfo> callers = methodsCalledInsidePrivilegedAction.get(calleeInfo.calledMethod);
        if (callers == null) {
            return null;
        }

        for (CallerInfo callerInfo: callers) {
            if (isTheSame(callerMethod, callerInfo, calleeInfo, action)) {
                return callerInfo;
            }
        }
        return null;
    }

    private boolean isTheSame(Method callerMethod, CallerInfo inside, CalleeInfo outside, Taint action) {
        for (UnknownSource inSrc: inside.calledOn.getSources()) {
            if (inSrc.getSourceType() != UnknownSourceType.FIELD) {
                continue;
            }

            if (inSrc.getSignatureField().equals(action.getRealInstanceClassName() + ".val$"
                    + callerMethod.getLocalVariableTable()
                            .getLocalVariableTable()[outside.calledOn.getVariableIndex()].getName())) {
                return true;
            }
        }
        return false;
    }

    private void reportBug(ClassContext classContext, Method method, InstructionHandle handle, CallPair callPair) {
        BugInstance bug = new BugInstance(this, "POSSIBLE_UNTRUSTED_OBJECT_INJECTION", NORMAL_PRIORITY)
                .addClassAndMethod(classContext.getJavaClass(), method)
                .addSourceLine(classContext, method, handle)
                .addClass(callPair.outside.calledClass.getClassName())
                .addCalledMethod(callPair.outside.calledClass.getClassName(), callPair.outside.calledMethod.getName(),
                        callPair.outside.calledMethod.getSignature(), callPair.outside.calledMethod.isStatic())
                .addSourceLine(callPair.outside.classContext, callPair.outside.callerMethod, callPair.outside.handle)
                .addSourceLine(callPair.inside.classContext, callPair.inside.callerMethod, callPair.inside.handle);
        bugReporter.reportBug(bug);
    }

    public void cleanupMethodsCalledOnTainted(Method method) {
        nonFinalMethodsCalledOnTainted.remove(method);
    }
}