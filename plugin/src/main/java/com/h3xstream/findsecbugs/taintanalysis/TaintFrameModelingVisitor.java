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

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.ba.AbstractFrameModelingVisitor;
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
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.StoreInstruction;

/**
 * Visitor to make instruction transfer of taint values easier
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintFrameModelingVisitor extends AbstractFrameModelingVisitor<Taint, TaintFrame> {

    private static final String TO_STRING_METHOD = "toString()Ljava/lang/String;";
    private static final String EQUALS_METHOD = "equals(Ljava/lang/Object;)Z";
    private static final Set<String> SAFE_OBJECT_TYPES;
    private static final Set<String> IMMUTABLE_OBJECT_TYPES;
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
        // these data types are not modified, when passed as a parameter to an unknown method
        IMMUTABLE_OBJECT_TYPES = new HashSet<String>(SAFE_OBJECT_TYPES.size() + 1);
        IMMUTABLE_OBJECT_TYPES.addAll(SAFE_OBJECT_TYPES);
        IMMUTABLE_OBJECT_TYPES.add("Ljava/lang/String;");
    }

    public TaintFrameModelingVisitor(ConstantPoolGen cpg, MethodDescriptor method,
            TaintMethodSummaryMap methodSummaries) {
        super(cpg);
        this.methodDescriptor = method;
        this.methodSummaries = methodSummaries;
        this.parameterStackSize = getParameterStackSize(method.getSignature(), method.isStatic());
        this.analyzedMethodSummary = new TaintMethodSummary();
        this.writtenIndeces = new HashSet<Integer>();
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

    private static Collection<Integer> getMutableStackIndices(String signature) {
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
            indices.set(i, stackIndex - indices.get(i) - 1);
        }
        return indices;
    }


    @Override
    public Taint getDefaultValue() {
        return new Taint(Taint.State.UNKNOWN);
    }

/* Uncomment to view most of the instruction as they are process by the TaintFrameModelingVisitor.
    @Override
    public void visitStackProducer(StackProducer obj) {
        if(obj instanceof Instruction) {
            ByteCode.printOpCode((Instruction) obj, cpg);
        }
    }
*/

    @Override
    public void visitLDC(LDC ldc) {
        Object value = ldc.getValue(cpg);
        if(value instanceof String) {
            pushSafe("\"" + (String) value + "\"");
        }
        else {
            pushSafe("LDC " + ldc.getType(cpg).getSignature());
        }

    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        // double and long type takes two slots in BCEL
        pushSafe("partial long/double");
        pushSafe("partial long/double");
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        getFrame().pushValue(new Taint(Taint.State.NULL).setDebugInfo("NULL"));
    }

    @Override
    public void visitNEW(NEW obj) {

        ObjectType type = obj.getLoadClassType(cpg);
        pushSafe("new " + type.getClassName() + "()");
    }


    @Override
    public void handleStoreInstruction(StoreInstruction storeIns) {
        try {
            int numConsumed = storeIns.consumeStack(cpg);
            if (numConsumed == Constants.UNPREDICTABLE) {
                throw new InvalidBytecodeException("Unpredictable stack consumption");
            }
            int index = storeIns.getIndex();
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
    public void handleLoadInstruction(LoadInstruction loadIns) {
        int numProduced = loadIns.produceStack(cpg);
        if (numProduced == Constants.UNPREDICTABLE) {
            throw new InvalidBytecodeException("Unpredictable stack production");
        }
        int index = loadIns.getIndex() + numProduced;
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
    public void visitANEWARRAY(ANEWARRAY obj) {
        try {
            getFrame().popValue();
            pushSafe("new " + obj.getLoadClassType(cpg) + "[]");
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
            // when varargs are used, taint is transfered to duplicated value too
            transferTaint(arrayTaint, merge);
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("Not enough values on the stack", ex);
        }
    }

    @Override
    public void visitAALOAD(AALOAD obj) {
        try {
            getFrame().popValue(); // array index
            Taint arrayTaint = getFrame().popValue();
            // just transfer the taint from array to value at any index
            getFrame().pushValue(new Taint(arrayTaint));
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
     * @param invoke Invoke instruction
     */
    private void visitInvoke(InvokeInstruction invoke) {
        TaintMethodSummary methodSummary = getMethodSummary(invoke);
        Taint taint = getMethodTaint(methodSummary);

        //Keep the source method name
        taint.setDebugInfo(invoke.getMethodName(cpg) + "()");

        if (taint.isUnknown()) {
            taint.addTaintLocation(getTaintLocation(), false);
        }
        taintMutableArguments(methodSummary, invoke);
        transferTaintToMutables(methodSummary, taint);
        modelInstruction(invoke, getNumWordsConsumed(invoke), getNumWordsProduced(invoke), taint);
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
        if (methodSummary != null) {
            return methodSummary;
        }
        if (TO_STRING_METHOD.equals(methodNameWithSig)) {
            return TaintMethodSummary.DEFAULT_TOSTRING_SUMMARY;
        }
        if (EQUALS_METHOD.equals(methodNameWithSig)) {
            return TaintMethodSummary.DEFAULT_EQUALS_SUMMARY;
        }
        if (Constants.CONSTRUCTOR_NAME.equals(methodName)
                && !SAFE_OBJECT_TYPES.contains("L" + className + ";")) {
            int stackSize = getParameterStackSize(signature, obj instanceof INVOKESTATIC);
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
            Taint taintMerge = mergeTransferParameters(taint.getTaintParameters());
            return Taint.merge(taint.getNonParametricTaint(), taintMerge);
        }
        if (taint.isTainted()) {
            taint = new Taint(taint);
            // we do not want to add locations to the method summary
            taint.addTaintLocation(getTaintLocation(), true);
        }
        assert taint != null;
        return taint;
    }

    private void taintMutableArguments(TaintMethodSummary methodSummary, InvokeInstruction obj) {
        if (methodSummary != null
                && methodSummary != TaintMethodSummary.SAFE_SUMMARY
                && !Constants.CONSTRUCTOR_NAME.equals(obj.getMethodName(cpg))) {
            return;
        }
        Collection<Integer> mutableStackIndices = getMutableStackIndices(obj.getSignature(cpg));
        for (Integer index : mutableStackIndices) {
            try {
                Taint stackValue = getFrame().getStackValue(index);
                stackValue.setState(Taint.State.merge(stackValue.getState(), Taint.State.UNKNOWN));
            } catch (DataflowAnalysisException ex) {
                throw new InvalidBytecodeException("Not enough values on the stack", ex);
            }
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
        if (methodSummary == null || !methodSummary.hasMutableStackIndeces()) {
            return;
        }
        try {
            for (Integer mutableStackIndex : methodSummary.getMutableStackIndeces()) {
                Taint stackValue = getFrame().getStackValue(mutableStackIndex);
                // needed especially for constructors
                transferTaint(stackValue, taint);
            }
        } catch (DataflowAnalysisException ex) {
            throw new RuntimeException("Bad mutable stack index specification", ex);
        }
    }

    private void transferTaint(Taint to, Taint from) {
        to.setState(from.getState());
        if (from.hasTaintParameters()) {
            for (Integer taintParameter : from.getTaintParameters()) {
                to.addTaintParameter(taintParameter);
            }
        }
        to.setNonParametricTaint(from.getNonParametricTaint());
        for (TaintLocation location : from.getTaintedLocations()) {
            to.addTaintLocation(location, true);
        }
        for (TaintLocation location : from.getPossibleTaintedLocations()) {
            to.addTaintLocation(location, false);
        }
        if (to.hasValidLocalVariableIndex()) {
            int index = to.getLocalVariableIndex();
            getFrame().setValue(index, from);
        } // else we are not able to transfer taint to a local variable
    }

    private void pushSafe(String debugInfo) {
        getFrame().pushValue(new Taint(Taint.State.SAFE).setDebugInfo(debugInfo));
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

    /**
     * For debugging purpose.
     */
    private void printStackState() {

        try {
            System.out.println("============================");
            System.out.println("[[ Stack ]]");
            int stackDepth = getFrame().getStackDepth();
            for (int i = 0; i < stackDepth; i++) {
                Taint taintValue = getFrame().getStackValue(i);
                System.out.println(String.format("%s. %s {%s}", i, taintValue.getState().toString(), taintValue.getDebugInfo()));
            }
            if(stackDepth == 0) {
                System.out.println("Empty");
            }
            System.out.println("============================");

        }
        catch (DataflowAnalysisException e) {
            System.out.println("Oups "+e.getMessage());
        }

    }
}
