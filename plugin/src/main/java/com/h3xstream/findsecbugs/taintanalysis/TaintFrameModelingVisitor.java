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
package com.h3xstream.findsecbugs.taintanalysis;

import edu.umd.cs.findbugs.ba.AbstractFrameModelingVisitor;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.InvalidBytecodeException;
import edu.umd.cs.findbugs.ba.generic.GenericSignatureParser;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.util.ClassName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.StoreInstruction;

/**
 * Visitor to make instruction transfer of taint values easier
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintFrameModelingVisitor extends AbstractFrameModelingVisitor<Taint, TaintFrame> {

    private static final Set<String> SAFE_OBJECT_TYPES;
    private static final Set<String> IMMUTABLE_OBJECT_TYPES;
    private final MethodDescriptor methodDescriptor;
    private final TaintMethodSummaryMap methodSummaries;
    private final TaintMethodSummary analyzedMethodSummary;

    static {
        // these data types cannot have taint state other than SAFE or NULL
        SAFE_OBJECT_TYPES = new HashSet<String>(9);
        SAFE_OBJECT_TYPES.add("Ljava/lang/Boolean;");
        SAFE_OBJECT_TYPES.add("Ljava/lang/Character;");
        SAFE_OBJECT_TYPES.add("Ljava/lang/Double;");
        SAFE_OBJECT_TYPES.add("Ljava/lang/Float;");
        SAFE_OBJECT_TYPES.add("Ljava/lang/Integer;");
        SAFE_OBJECT_TYPES.add("Ljava/lang/Long;");
        SAFE_OBJECT_TYPES.add("Ljava/lang/Byte;");
        SAFE_OBJECT_TYPES.add("Ljava/lang/Short;");
        SAFE_OBJECT_TYPES.add("Ljava/math/BigDecimal;");
        // these data types are not modified, when passed as a parameter to an unknown method
        IMMUTABLE_OBJECT_TYPES = new HashSet<String>(SAFE_OBJECT_TYPES.size() + 7);
        IMMUTABLE_OBJECT_TYPES.addAll(SAFE_OBJECT_TYPES);
        IMMUTABLE_OBJECT_TYPES.add("Ljava/lang/String;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/math/BigInteger;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/io/File;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/util/Locale;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/Inet4Address;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/Inet6Address;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/InetSocketAddress;");
    }

    public TaintFrameModelingVisitor(ConstantPoolGen cpg, MethodDescriptor method,
            TaintMethodSummaryMap methodSummaries) {
        super(cpg);
        if (method == null) {
            throw new NullPointerException("null method descriptor");
        }
        if (methodSummaries == null) {
            throw new NullPointerException("null method summaries");
        }
        this.methodDescriptor = method;
        this.methodSummaries = methodSummaries;
        this.analyzedMethodSummary = new TaintMethodSummary(false);
    }

    private static Collection<Integer> getMutableStackIndices(String signature) {
        assert signature != null && !signature.isEmpty();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        int stackIndex = 0;
        GenericSignatureParser parser = new GenericSignatureParser(signature);
        Iterator<String> iterator = parser.parameterSignatureIterator();
        while (iterator.hasNext()) {
            String parameter = iterator.next();
            if ((parameter.startsWith("L") || parameter.startsWith("["))
                    && !IMMUTABLE_OBJECT_TYPES.contains(parameter)) {
                indices.add(stackIndex);
            }
            if (parameter.equals("D") || parameter.equals("J")) {
                // double and long types takes two slots
                stackIndex += 2;
            } else {
                stackIndex++;
            }
        }
        for (int i = 0; i < indices.size(); i++) {
            int reverseIndex = stackIndex - indices.get(i) - 1;
            assert reverseIndex >= 0;
            indices.set(i, reverseIndex);
        }
        return indices;
    }

    @Override
    public Taint getDefaultValue() {
        return new Taint(Taint.State.UNKNOWN);
    }

    @Override
    public void visitLDC(LDC obj) {
        pushSafe();
    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        // double and long type takes two slots in BCEL
        pushSafe();
        pushSafe();
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        getFrame().pushValue(new Taint(Taint.State.NULL));
    }

    @Override
    public void visitNEW(NEW obj) {
        Taint taint = new Taint(Taint.State.SAFE);
        taint.setRealInstanceClass(obj.getLoadClassType(cpg));
        getFrame().pushValue(taint);
    }

    @Override
    public void handleStoreInstruction(StoreInstruction obj) {
        try {
            int numConsumed = obj.consumeStack(cpg);
            if (numConsumed == Constants.UNPREDICTABLE) {
                throw new InvalidBytecodeException("Unpredictable stack consumption");
            }
            int index = obj.getIndex();
            while (numConsumed-- > 0) {
                Taint value = new Taint(getFrame().popValue());
                value.setVariableIndex(index);
                getFrame().setValue(index++, value);
            }
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException(ex.toString(), ex);
        }
    }

    @Override
    public void handleLoadInstruction(LoadInstruction obj) {
        int numProduced = obj.produceStack(cpg);
        if (numProduced == Constants.UNPREDICTABLE) {
            throw new InvalidBytecodeException("Unpredictable stack production");
        }
        int index = obj.getIndex() + numProduced;
        while (numProduced-- > 0) {
            Taint value = getFrame().getValue(--index);
            assert value.hasValidVariableIndex() : "index not set in " + methodDescriptor;
            assert index == value.getVariableIndex() : "bad index in " + methodDescriptor;
            getFrame().pushValue(new Taint(value));
        }
    }

    @Override
    public void visitINVOKEINTERFACE(INVOKEINTERFACE obj) {
        visitInvoke(obj);
    }

    @Override
    public void visitINVOKESPECIAL(INVOKESPECIAL obj) {
        visitInvoke(obj);
    }

    @Override
    public void visitINVOKESTATIC(INVOKESTATIC obj) {
        visitInvoke(obj);
    }

    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {
        visitInvoke(obj);
    }

    @Override
    public void visitANEWARRAY(ANEWARRAY obj) {
        try {
            getFrame().popValue();
            pushSafe();
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("Array length not in the stack", ex);
        }
    }

    @Override
    public void visitAASTORE(AASTORE obj) {
        try {
            Taint valueTaint = getFrame().popValue();
            getFrame().popValue(); // array index
            Taint arrayTaint = getFrame().popValue();
            Taint merge = Taint.merge(valueTaint, arrayTaint);
            setLocalVariableTaint(merge, arrayTaint);
            Taint stackTop = null;
            if (getFrame().getStackDepth() > 0) {
                stackTop = getFrame().getTopValue();
            }
            // varargs use duplicated values
            if (stackTop == arrayTaint) {
                getFrame().popValue();
                getFrame().pushValue(new Taint(merge));
            }
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("Not enough values on the stack", ex);
        }
    }

    @Override
    public void visitAALOAD(AALOAD obj) {
        try {
            getFrame().popValue(); // array index
            // just transfer the taint from array to value at any index
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("Not enough values on the stack", ex);
        }
    }

    @Override
    public void visitCHECKCAST(CHECKCAST obj) {
        // keep the top of stack unchanged
    }

    /**
     * Regroup the method invocations (INVOKEINTERFACE, INVOKESPECIAL,
     * INVOKESTATIC, INVOKEVIRTUAL)
     *
     * @param obj one of the invoke instructions
     */
    private void visitInvoke(InvokeInstruction obj) {
        assert obj != null;
        TaintMethodSummary methodSummary = getMethodSummary(obj);
        ObjectType realInstanceClass = (methodSummary == null) ?
                null : methodSummary.getOutputTaint().getRealInstanceClass();
        Taint taint = getMethodTaint(methodSummary);
        assert taint != null;
        if (taint.isUnknown()) {
            taint.addLocation(getTaintLocation(), false);
        }
        taintMutableArguments(methodSummary, obj);
        transferTaintToMutables(methodSummary, taint); // adds variable index to taint too
        Taint taintCopy = new Taint(taint);
        // return type is not the instance type always
        taintCopy.setRealInstanceClass(realInstanceClass);
        modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), taintCopy);
    }

    private TaintMethodSummary getMethodSummary(InvokeInstruction obj) {
        String signature = obj.getSignature(cpg);
        String returnType = getReturnType(signature);
        if (SAFE_OBJECT_TYPES.contains(returnType)) {
            return TaintMethodSummary.SAFE_SUMMARY;
        }
        String className = getInstanceClassName(obj);
        String methodName = obj.getMethodName(cpg);
        String methodId = "." + methodName + signature;
        TaintMethodSummary summary = methodSummaries.get(className.concat(methodId));
        if (summary != null) {
            return summary;
        }
        summary = getSuperMethodSummary(className, methodId);
        if (summary != null) {
            return summary;
        }
        if (Constants.CONSTRUCTOR_NAME.equals(methodName)
                && !SAFE_OBJECT_TYPES.contains("L" + className + ";")) {
            try {
                int stackSize = getFrame().getNumArgumentsIncludingObjectInstance(obj, cpg);
                return TaintMethodSummary.getDefaultConstructorSummary(stackSize);
            } catch (DataflowAnalysisException ex) {
                throw new InvalidBytecodeException(ex.getMessage(), ex);
            }
        }
        return null;
    }
    
    private String getInstanceClassName(InvokeInstruction invoke) {
        try {
            int instanceIndex = getFrame().getNumArgumentsIncludingObjectInstance(invoke, cpg) - 1;
            if (instanceIndex != -1) {
                assert instanceIndex < getFrame().getStackDepth();
                Taint instanceTaint = getFrame().getStackValue(instanceIndex);
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
    
    private TaintMethodSummary getSuperMethodSummary(String className, String methodId) {
        try {
            JavaClass javaClass = Repository.lookupClass(className);
            assert javaClass != null;
            TaintMethodSummary summary = getSuperMethodSummary(javaClass.getSuperClasses(), methodId);
            if (summary != null) {
                return summary;
            }
            return getSuperMethodSummary(javaClass.getAllInterfaces(), methodId);
        } catch (ClassNotFoundException ex) {
            AnalysisContext.reportMissingClass(ex);
            return null;
        }
    }
    
    private TaintMethodSummary getSuperMethodSummary(JavaClass[] javaClasses, String method) {
        assert javaClasses != null;
        for (JavaClass classOrInterface : javaClasses) {
            String fullMethodName = classOrInterface.getClassName().replace('.', '/').concat(method);
            TaintMethodSummary summary = methodSummaries.get(fullMethodName);
            if (summary != null) {
                return summary;
            }
        }
        return null;
    }
    
    private static String getReturnType(String signature) {
        assert signature != null && signature.contains(")");
        return signature.substring(signature.indexOf(')') + 1);
    }

    private Taint getMethodTaint(TaintMethodSummary methodSummary) {
        if (methodSummary == null) {
            return getDefaultValue();
        }
        Taint taint = methodSummary.getOutputTaint();
        assert taint != null;
        assert taint != methodSummary.getOutputTaint() : "defensive copy not made";
        if (taint.isUnknown() && taint.hasParameters()) {
            Taint merge = mergeTransferParameters(taint.getParameters());
            assert merge != null;
            return Taint.merge(Taint.valueOf(taint.getNonParametricState()), merge);
        }
        if (taint.isTainted()) {
            taint.addLocation(getTaintLocation(), true);
        }
        return taint;
    }

    private void taintMutableArguments(TaintMethodSummary methodSummary, InvokeInstruction obj) {
        if (methodSummary != null && methodSummary.isConfigured()) {
            return;
        }
        Collection<Integer> mutableStackIndices = getMutableStackIndices(obj.getSignature(cpg));
        for (Integer index : mutableStackIndices) {
            assert index >= 0 && index < getFrame().getStackDepth();
            try {
                Taint stackValue = getFrame().getStackValue(index);
                Taint taint = Taint.merge(stackValue, getDefaultValue());
                if (stackValue.hasValidVariableIndex()) {
                    // set back the index removed during merging
                    taint.setVariableIndex(stackValue.getVariableIndex());
                }
                taint.setRealInstanceClass(stackValue.getRealInstanceClass());
                taint.addLocation(getTaintLocation(), false);
                getFrame().setValue(getFrame().getStackLocation(index), taint);
                setLocalVariableTaint(taint, taint);
            } catch (DataflowAnalysisException ex) {
                throw new InvalidBytecodeException("Not enough values on the stack", ex);
            }
        }
    }

    private Taint mergeTransferParameters(Collection<Integer> transferParameters) {
        assert transferParameters != null && !transferParameters.isEmpty();
        Taint taint = null;
        for (Integer transferParameter : transferParameters) {
            try {
                Taint value = getFrame().getStackValue(transferParameter);
                taint = Taint.merge(taint, value);
            } catch (DataflowAnalysisException ex) {
                throw new RuntimeException("Bad transfer parameter specification", ex);
            }
        }
        assert taint != null;
        return taint;
    }

    private void transferTaintToMutables(TaintMethodSummary methodSummary, Taint taint) {
        assert taint != null;
        if (methodSummary == null || !methodSummary.hasMutableStackIndeces()) {
            return;
        }
        try {
            int stackDepth = getFrame().getStackDepth();
            for (Integer mutableStackIndex : methodSummary.getMutableStackIndeces()) {
                assert mutableStackIndex >= 0;
                if (mutableStackIndex >= stackDepth) {
                    if (!Constants.CONSTRUCTOR_NAME.equals(methodDescriptor.getName())) {
                        assert false : "Out of bounds mutables in " + methodDescriptor;
                    }
                    continue; // ignore if assertions disabled or if in constructor
                }
                Taint stackValue = getFrame().getStackValue(mutableStackIndex);
                setLocalVariableTaint(taint, stackValue);
                Taint taintCopy = new Taint(taint);
                // do not set instance to return values, can be different type
                taintCopy.setRealInstanceClass(stackValue.getRealInstanceClass());
                getFrame().setValue(getFrame().getStackLocation(mutableStackIndex), taintCopy);
            }
        } catch (DataflowAnalysisException ex) {
            assert false : ex.getMessage(); // stack depth is checked
        }
    }

    private void setLocalVariableTaint(Taint valueTaint, Taint indexTaint) {
        assert valueTaint != null && indexTaint != null;
        if (!indexTaint.hasValidVariableIndex()) {
            return;
        }
        int index = indexTaint.getVariableIndex();
        if (index >= getFrame().getNumLocals()) {
            assert false : "Out of bounds local variable index in " + methodDescriptor;
            return; // ignore if assertions disabled
        }
        valueTaint.setVariableIndex(index);
        getFrame().setValue(index, valueTaint);
    }

    private void pushSafe() {
        getFrame().pushValue(new Taint(Taint.State.SAFE));
    }

    private TaintLocation getTaintLocation() {
        return new TaintLocation(methodDescriptor, getLocation().getHandle().getPosition());
    }

    @Override
    public void visitARETURN(ARETURN obj) {
        try {
            Taint returnTaint = getFrame().getTopValue();
            Taint currentTaint = analyzedMethodSummary.getOutputTaint();
            analyzedMethodSummary.setOuputTaint(Taint.merge(returnTaint, currentTaint));
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("empty stack before reference return", ex);
        }
        handleNormalInstruction(obj);
    }

    public void finishAnalysis() {
        assert analyzedMethodSummary != null;
        Taint outputTaint = analyzedMethodSummary.getOutputTaint();
        if (outputTaint == null) {
            // void methods
            return;
        }
        String returnType = getReturnType(methodDescriptor.getSignature());
        if (SAFE_OBJECT_TYPES.contains(returnType) && outputTaint.getState() != Taint.State.NULL) {
            // we do not have to store summaries with safe output
            return;
        }
        String realInstanceClassName = outputTaint.getRealInstanceClassName();
        if (returnType.equals("L" + realInstanceClassName + ";")) {
            // storing it in method summary is useless
            outputTaint.setRealInstanceClass(null);
            analyzedMethodSummary.setOuputTaint(outputTaint);
        }
        if (analyzedMethodSummary.isInformative()) {
            String fullMethodName = methodDescriptor.getSlashedClassName()
                    + "." + methodDescriptor.getName() + methodDescriptor.getSignature();
            methodSummaries.put(fullMethodName, analyzedMethodSummary);
        }
    }
}
