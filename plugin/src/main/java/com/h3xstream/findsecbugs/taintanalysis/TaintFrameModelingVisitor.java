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
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.InvalidBytecodeException;
import edu.umd.cs.findbugs.ba.generic.GenericSignatureParser;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.util.ClassName;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.bcel.Constants;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.StoreInstruction;

/**
 * Visitor to make instruction transfer of taint values easier
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintFrameModelingVisitor extends AbstractFrameModelingVisitor<Taint, TaintFrame> {

    private static final String TO_STRING_METHOD = "toString()Ljava/lang/String;";
    private static final Set<String> SAFE_OBJECT_TYPES;
    private final MethodDescriptor methodDescriptor;
    private final int parameterStackSize;
    private final TaintMethodSummaryMap methodSummaries;
    private final TaintMethodSummary analyzedMethodSummary;
    private final Set<Integer> writtenIndeces;
    
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
        SAFE_OBJECT_TYPES.add("Ljava/lang/BigDecimal;");
    }
    
    public TaintFrameModelingVisitor(ConstantPoolGen cpg, MethodDescriptor method,
            TaintMethodSummaryMap methodSummaries) {
        super(cpg);
        this.methodDescriptor = method;
        this.methodSummaries = methodSummaries;
        parameterStackSize = getParameterStackSize(method.getSignature(), method.isStatic());
        analyzedMethodSummary = new TaintMethodSummary();
        writtenIndeces = new HashSet<Integer>();
    }

    private static int getParameterStackSize(String signature, boolean isStatic) {
        // static methods does not have reference to this
        int stackSize = isStatic ? -1 : 0;
        GenericSignatureParser parser = new GenericSignatureParser(signature);
        Iterator<String> iterator = parser.parameterSignatureIterator();
        while (iterator.hasNext()) {
            String parameter = iterator.next();
            if (parameter.equals("D") || parameter.equals("J")) {
                // double and long types takes two slots
                stackSize += 2;
            } else {
                stackSize++;
            }
        }
        return stackSize;
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
        pushSafe();
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
                Taint value = getFrame().popValue();
                writtenIndeces.add(index);
                getFrame().setValue(index++, value);
            }
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException(ex.toString());
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
            // set local variable origin of a stack value
            value.setLocalVariableIndex(index);
            if (!writtenIndeces.contains(index)) {
                // it must be parameter if not written to local variable
                int stackOffset = parameterStackSize - index;
                assert stackOffset >= 0; // since there is unwritten index
                value.addTaintParameter(stackOffset);
            }
            getFrame().pushValue(value);
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
    public void visitAALOAD(AALOAD obj) {
        try {
            Taint arrayTaint = getFrame().getStackValue(1);
            Taint pushTaint = new Taint(arrayTaint);
            modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), pushTaint);
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("Not enough values on the stack", ex);
        }
    }
    
    private void visitInvoke(InvokeInstruction obj) {
        TaintMethodSummary methodSummary = getMethodSummary(obj);
        Taint taint = getMethodTaint(methodSummary);
        if (taint.isUnknown()) {
            taint.addTaintLocation(getTaintLocation(), false);
        }
        transferTaintToMutables(methodSummary, taint);
        modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), taint);
    }

    private TaintMethodSummary getMethodSummary(InvokeInstruction obj) {
        String signature = obj.getSignature(cpg);
        String returnType = getReturnType(signature);
        if (SAFE_OBJECT_TYPES.contains(returnType)) {
            return TaintMethodSummary.SAFE_SUMMARY;
        }
        String className = getSlashedClassName(obj);
        String methodName = obj.getMethodName(cpg);
        String methodNameWithSig = methodName + signature;
        String fullMethodName = className + "." + methodNameWithSig;
        TaintMethodSummary methodSummary = methodSummaries.get(fullMethodName);
        if (methodSummary == null && TO_STRING_METHOD.equals(methodNameWithSig)) {
            return TaintMethodSummary.DEFAULT_TOSTRING_SUMMARY;
        }
        if (methodSummary == null
                && Constants.CONSTRUCTOR_NAME.equals(methodName)
                && !SAFE_OBJECT_TYPES.contains("L" + className + ";")) {
            int stackSize = getParameterStackSize(signature, false);
            return TaintMethodSummary.getDefaultConstructorSummary(stackSize);
        }
        return methodSummary;
    }
    
    private static String getReturnType(String signature) {
        return signature.substring(signature.indexOf(')') + 1);
    }
    
    private String getSlashedClassName(FieldOrMethod obj) {
        String className = obj.getReferenceType(cpg).toString();
        return ClassName.toSlashedClassName(className);
    }
    
    private Taint getMethodTaint(TaintMethodSummary methodSummary) {
        if (methodSummary == null) {
            return getDefaultValue();
        }
        Taint taint = methodSummary.getOutputTaint();
        if (taint.isUnknown() && taint.hasTaintParameters()) {
            return mergeTransferParameters(taint.getTaintParameters());
        } else {
            if (taint.isTainted()) {
                taint = new Taint(taint);
                // we do not want to add locations to the method summary
                taint.addTaintLocation(getTaintLocation(), true);
            }
            return taint;
        }
    }
    
    private Taint mergeTransferParameters(Collection<Integer> transferParameters) {
        Taint taint = null;
        assert !transferParameters.isEmpty();
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
    
    private void transferTaintToMutables(TaintMethodSummary methodSummary, Taint taint) throws RuntimeException {
        if (methodSummary == null || !methodSummary.hasMutableStackIndex()) {
            return;
        }
        int mutableStackIndex = methodSummary.getMutableStackIndex();
        try {
            Taint stackValue = getFrame().getStackValue(mutableStackIndex);
            // needed especially for constructors
            stackValue.setState(taint.getState());
            if (taint.hasTaintParameters()) {
                for (Integer taintParameter : taint.getTaintParameters()) {
                    stackValue.addTaintParameter(taintParameter);
                }
            }
            for (TaintLocation location : taint.getTaintedLocations()) {
                stackValue.addTaintLocation(location, true);
            }
            for (TaintLocation location : taint.getPossibleTaintedLocations()) {
                stackValue.addTaintLocation(location, false);
            }
            if (stackValue.hasValidLocalVariableIndex()) {
                int index = stackValue.getLocalVariableIndex();
                getFrame().setValue(index, taint);
            }
            // else we are not able to transfer taint to a local variable
        } catch (DataflowAnalysisException ex) {
            throw new RuntimeException("Bad mutable stack index specification", ex);
        }
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
    
    public TaintMethodSummary getAnalyzedMethodSummary() {
        assert analyzedMethodSummary != null;
        if (SAFE_OBJECT_TYPES.contains(getReturnType(methodDescriptor.getSignature()))
                && (analyzedMethodSummary.getOutputTaint() == null
                || analyzedMethodSummary.getOutputTaint().getState() != Taint.State.NULL)) {
            return TaintMethodSummary.SAFE_SUMMARY;
        }
        return analyzedMethodSummary;
    }
}
