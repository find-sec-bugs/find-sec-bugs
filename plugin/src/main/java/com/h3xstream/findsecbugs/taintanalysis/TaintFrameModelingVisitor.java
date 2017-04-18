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

import com.h3xstream.findsecbugs.FindSecBugsGlobalConfig;
import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.ba.AbstractFrameModelingVisitor;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.InvalidBytecodeException;
import edu.umd.cs.findbugs.ba.generic.GenericSignatureParser;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.util.ClassName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.ICONST;
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
import org.apache.bcel.generic.SIPUSH;
import org.apache.bcel.generic.StoreInstruction;

/**
 * Visitor to make instruction transfer of taint values easier
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintFrameModelingVisitor extends AbstractFrameModelingVisitor<Taint, TaintFrame> {

    private static final Map<String, Taint.Tag> REPLACE_TAGS;
    private final MethodDescriptor methodDescriptor;
    private final TaintConfig taintConfig;
    private final TaintMethodConfig analyzedMethodConfig;

    static {
        REPLACE_TAGS = new HashMap<String, Taint.Tag>();
        REPLACE_TAGS.put("\r", Taint.Tag.CR_ENCODED);
        REPLACE_TAGS.put("\n", Taint.Tag.LF_ENCODED);
        REPLACE_TAGS.put("\"", Taint.Tag.QUOTE_ENCODED);
        REPLACE_TAGS.put("'", Taint.Tag.APOSTROPHE_ENCODED);
        REPLACE_TAGS.put("<", Taint.Tag.LT_ENCODED);
    }

    /**
     * Constructs the object and stores the parameters
     * 
     * @param cpg constant pool gen for super class
     * @param method descriptor of analysed method
     * @param taintConfig current configured and derived taint summaries
     * @throws NullPointerException if arguments method or taintConfig is null
     */
    public TaintFrameModelingVisitor(ConstantPoolGen cpg, MethodDescriptor method,
            TaintConfig taintConfig) {
        super(cpg);
        if (method == null) {
            throw new NullPointerException("null method descriptor");
        }
        if (taintConfig == null) {
            throw new NullPointerException("null taint config");
        }
        this.methodDescriptor = method;
        this.taintConfig = taintConfig;
        this.analyzedMethodConfig = new TaintMethodConfig(false);
    }

    private Collection<Integer> getMutableStackIndices(String signature) {
        assert signature != null && !signature.isEmpty();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        int stackIndex = 0;
        GenericSignatureParser parser = new GenericSignatureParser(signature);
        Iterator<String> iterator = parser.parameterSignatureIterator();
        while (iterator.hasNext()) {
            String parameter = iterator.next();
            if ((parameter.startsWith("L") || parameter.startsWith("["))
                    && !taintConfig.isClassImmutable(parameter)) {
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
    public void analyzeInstruction(Instruction ins) throws DataflowAnalysisException {
        //Print the bytecode instruction if it is globally configured
        if (FindSecBugsGlobalConfig.getInstance().isDebugPrintInvocationVisited()
                && ins instanceof InvokeInstruction) {
            ByteCode.printOpCode(ins, cpg);
        } else if (FindSecBugsGlobalConfig.getInstance().isDebugPrintInstructionVisited()) {
            ByteCode.printOpCode(ins, cpg);
        }
        super.analyzeInstruction(ins);
    }

    @Override
    public Taint getDefaultValue() {
        return new Taint(Taint.State.UNKNOWN);
    }

    @Override
    public void visitLDC(LDC ldc) {
        Taint taint = new Taint(Taint.State.SAFE);
        Object value = ldc.getValue(cpg);
        if (value instanceof String) {
            taint.setConstantValue((String) value);
        }
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            if (value instanceof String) {
                taint.setDebugInfo("\"" + value + "\"");
            } else {
                taint.setDebugInfo("LDC " + ldc.getType(cpg).getSignature());
            }
        }
        getFrame().pushValue(taint);
    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        // double and long type takes two slots in BCEL
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            pushSafeDebug("partial long/double");
            pushSafeDebug("partial long/double");
        } else {
            pushSafe();
            pushSafe();
        }
    }

    @Override
    public void visitBIPUSH(BIPUSH obj) {
        Taint taint = new Taint(Taint.State.SAFE);
        // assume each pushed byte is a char
        taint.setConstantValue(String.valueOf((char) obj.getValue().byteValue()));
        getFrame().pushValue(taint);
    }
    
    @Override
    public void visitSIPUSH(SIPUSH obj) {
        Taint taint = new Taint(Taint.State.SAFE);
        // assume each pushed short is a char (for non-ASCII characters)
        taint.setConstantValue(String.valueOf((char) obj.getValue().shortValue()));
        getFrame().pushValue(taint);
    }

    @Override
    public void visitGETSTATIC(GETSTATIC obj) {
        // Scala uses some classes to represent null instances of objects
        // If we find one of them, we will handle it as a Java Null
        if (obj.getLoadClassType(getCPG()).getSignature().equals("Lscala/collection/immutable/Nil$;")) {

            if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
                getFrame().pushValue(new Taint(Taint.State.NULL).setDebugInfo("NULL"));
            } else {
                getFrame().pushValue(new Taint(Taint.State.NULL));
            }
        } else {
            super.visitGETSTATIC(obj);
        }
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            getFrame().pushValue(new Taint(Taint.State.NULL).setDebugInfo("NULL"));
        } else {
            getFrame().pushValue(new Taint(Taint.State.NULL));
        }
    }

    @Override
     public void visitICONST(ICONST obj) {
        Taint t = new Taint(Taint.State.SAFE);
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            t.setDebugInfo("" + obj.getValue().intValue());
        }
        getFrame().pushValue(t);
    }

    @Override
    public void visitGETFIELD(GETFIELD obj) {
        Taint.State state = taintConfig.getClassTaintState(obj.getSignature(cpg), Taint.State.UNKNOWN);
        Taint taint = new Taint(state);

        if (!state.equals(Taint.State.SAFE)){
            taint.addLocation(getTaintLocation(), false);
        }
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            taint.setDebugInfo("." + obj.getFieldName(cpg));
        }
        modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), taint);
    }

    @Override
    public void visitNEW(NEW obj) {
        Taint taint = new Taint(Taint.State.SAFE);
        ObjectType type = obj.getLoadClassType(cpg);
        taint.setRealInstanceClass(type);
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            taint.setDebugInfo("new " + type.getClassName() + "()");
        }
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
            if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
                pushSafeDebug("new " + obj.getLoadClassType(cpg).getClassName() + "[]");
            } else {
                pushSafe();
            }
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
        // cast to a safe object type
        ObjectType objectType = obj.getLoadClassType(cpg);
        if (objectType == null) {
            return;
        }

        String objectTypeSignature = objectType.getSignature();

        if(!taintConfig.isClassTaintSafe(objectTypeSignature)) {
            return;
        }

        try {
            getFrame().popValue();
            pushSafe();
        }
        catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("empty stack for checkcast", ex);
        }
    }

    @Override
    public void visitARETURN(ARETURN obj) {
        try {
            Taint returnTaint = getFrame().getTopValue();
            Taint currentTaint = analyzedMethodConfig.getOutputTaint();
            analyzedMethodConfig.setOuputTaint(Taint.merge(returnTaint, currentTaint));
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("empty stack before reference return", ex);
        }
        handleNormalInstruction(obj);
    }

    /**
     * Regroup the method invocations (INVOKEINTERFACE, INVOKESPECIAL,
     * INVOKESTATIC, INVOKEVIRTUAL)
     *
     * @param obj one of the invoke instructions
     */
    private void visitInvoke(InvokeInstruction obj) {
        assert obj != null;
        try {
            TaintMethodConfig methodConfig = getMethodConfig(obj);
            ObjectType realInstanceClass = (methodConfig == null) ?
                    null : methodConfig.getOutputTaint().getRealInstanceClass();
            Taint taint = getMethodTaint(methodConfig);
            assert taint != null;
            if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
                taint.setDebugInfo(obj.getMethodName(cpg) + "()");
            }
            if (taint.isUnknown()) {
                taint.addLocation(getTaintLocation(), false);
            }
            taintMutableArguments(methodConfig, obj);
            transferTaintToMutables(methodConfig, taint); // adds variable index to taint too
            Taint taintCopy = new Taint(taint);
            // return type is not always the instance type
            taintCopy.setRealInstanceClass(realInstanceClass);
            modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), taintCopy);
        } catch (Exception e) {
            String className = ClassName.toSlashedClassName(obj.getReferenceType(cpg).toString());
            String methodName = obj.getMethodName(cpg);
            String signature = obj.getSignature(cpg);

            throw new RuntimeException("Unable to call " + className + '.' + methodName + signature, e);
        }
    }

    private TaintMethodConfig getMethodConfig(InvokeInstruction obj) {
        String signature = obj.getSignature(cpg);
        String returnType = getReturnType(signature);
        String className = getInstanceClassName(obj);
        String methodName = obj.getMethodName(cpg);
        String methodId = "." + methodName + signature;
        TaintMethodConfig config = taintConfig.getMethodConfig(getFrame(), methodDescriptor, className, methodId);
        if (config != null) {
            config = getConfigWithReplaceTags(config, className, methodName);
        }
        if (config != null && config.isConfigured()) {
            return config;
        }
        if (taintConfig.isClassTaintSafe(returnType)) {
            return TaintMethodConfig.SAFE_CONFIG;
        }
        if (config != null) {
            return config;
        }
        if (Constants.CONSTRUCTOR_NAME.equals(methodName)
                && !taintConfig.isClassTaintSafe("L" + className + ";")) {
            try {
                int stackSize = getFrame().getNumArgumentsIncludingObjectInstance(obj, cpg);
                return TaintMethodConfig.getDefaultConstructorConfig(stackSize);
            } catch (DataflowAnalysisException ex) {
                throw new InvalidBytecodeException(ex.getMessage(), ex);
            }
        }
        return null;
    }

    private TaintMethodConfig getConfigWithReplaceTags(
            TaintMethodConfig config, String className, String methodName) {
        if (!"java/lang/String".equals(className)) {
            return config;
        }
        boolean isRegex = "replaceAll".equals(methodName);
        if (!isRegex && !"replace".equals(methodName)) {
            // not a replace method
            return config;
        }
        try {
            String toReplace = getFrame().getStackValue(1).getConstantValue();
            if (toReplace == null) {
                // we don't know the exact value
                return config;
            }
            Taint taint = config.getOutputTaint();
            for (Map.Entry<String, Taint.Tag> replaceTag : REPLACE_TAGS.entrySet()) {
                String tagString = replaceTag.getKey();
                if ((isRegex && toReplace.contains(tagString))
                        || toReplace.equals(tagString)) {
                    taint.addTag(replaceTag.getValue());
                }
            }
            TaintMethodConfig configCopy = new TaintMethodConfig(config);
            configCopy.setOuputTaint(taint);
            return configCopy;
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException(ex.getMessage(), ex);
        }
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

    private static String getReturnType(String signature) {
        assert signature != null && signature.contains(")");
        return signature.substring(signature.indexOf(')') + 1);
    }

    private Taint getMethodTaint(TaintMethodConfig methodConfig) {
        if (methodConfig == null) {
            return getDefaultValue();
        }
        Taint taint = methodConfig.getOutputTaint();
        assert taint != null;
        assert taint != methodConfig.getOutputTaint() : "defensive copy not made";
        Taint taintCopy = new Taint(taint); 
        if (taint.isUnknown() && taint.hasParameters()) {
            Taint merge = mergeTransferParameters(taint.getParameters());
            assert merge != null;
            // merge removes tags so we made a taint copy before
            taint = Taint.merge(Taint.valueOf(taint.getNonParametricState()), merge);
        }
        if (taint.isTainted()) {
            taint.addLocation(getTaintLocation(), true);
        }
        // don't add tags to safe values
        if (!taint.isSafe() && taintCopy.hasTags()) {
            for (Taint.Tag tag : taintCopy.getTags()) {
                taint.addTag(tag);
            }
        }
        if (taintCopy.isRemovingTags()) {
            for (Taint.Tag tag : taintCopy.getTagsToRemove()) {
                taint.removeTag(tag);
            }
        }
        return taint;
    }

    private void taintMutableArguments(TaintMethodConfig methodConfig, InvokeInstruction obj) {
        if (methodConfig != null && methodConfig.isConfigured()) {
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

    private void transferTaintToMutables(TaintMethodConfig methodConfig, Taint taint) {
        assert taint != null;
        if (methodConfig == null || !methodConfig.hasMutableStackIndices()) {
            return;
        }
        try {
            int stackDepth = getFrame().getStackDepth();
            for (Integer mutableStackIndex : methodConfig.getMutableStackIndices()) {
                assert mutableStackIndex >= 0;
                if (mutableStackIndex >= stackDepth) {
                    if (!Constants.CONSTRUCTOR_NAME.equals(methodDescriptor.getName())
                            && !Constants.STATIC_INITIALIZER_NAME.equals(methodDescriptor.getName())) {
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

    /**
     * Push a value to the stack
     */
    private void pushSafe() {
        getFrame().pushValue(new Taint(Taint.State.SAFE));
    }

    /**
     * Push a value to the stack
     * The information passed will be viewable when the stack will be print. (See printStackState())
     * @param debugInfo String representation of the value push
     */
    private void pushSafeDebug(String debugInfo) {
        getFrame().pushValue(new Taint(Taint.State.SAFE).setDebugInfo(debugInfo));
    }

    private TaintLocation getTaintLocation() {
        return new TaintLocation(methodDescriptor, getLocation().getHandle().getPosition());
    }

    /**
     * This method must be called from outside at the end of the method analysis
     */
    public void finishAnalysis() {
        assert analyzedMethodConfig != null;
        Taint outputTaint = analyzedMethodConfig.getOutputTaint();
        if (outputTaint == null) {
            // void methods
            return;
        }
        String returnType = getReturnType(methodDescriptor.getSignature());
        if (taintConfig.isClassTaintSafe(returnType) && outputTaint.getState() != Taint.State.NULL) {
            // we do not have to store summaries with safe output
            return;
        }
        String realInstanceClassName = outputTaint.getRealInstanceClassName();
        if (returnType.equals("L" + realInstanceClassName + ";")) {
            // storing it in method summary is useless
            outputTaint.setRealInstanceClass(null);
            analyzedMethodConfig.setOuputTaint(outputTaint);
        }
        String className = methodDescriptor.getSlashedClassName();
        String methodId = "." + methodDescriptor.getName() + methodDescriptor.getSignature();
        if (analyzedMethodConfig.isInformative()
                || taintConfig.getSuperMethodConfig(className, methodId) != null) {
            String fullMethodName = className.concat(methodId);
            if (!taintConfig.containsKey(fullMethodName)) {
                // prefer configured summaries to derived
                taintConfig.put(fullMethodName, analyzedMethodConfig);
            }
        }
    }

}
