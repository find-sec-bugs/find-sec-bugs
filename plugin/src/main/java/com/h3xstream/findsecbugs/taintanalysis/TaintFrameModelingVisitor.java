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
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.InvalidBytecodeException;
import edu.umd.cs.findbugs.ba.generic.GenericSignatureParser;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.util.ClassName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.bcel.Constants;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
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

    private static final Set<String> SAFE_OBJECT_TYPES;
    private static final Set<String> IMMUTABLE_OBJECT_TYPES;
    private static final Map<String, Taint.Tag> REPLACE_TAGS;
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
        SAFE_OBJECT_TYPES.add("Ljava/util/Date;");
        SAFE_OBJECT_TYPES.add("Ljava/sql/Time;");
        SAFE_OBJECT_TYPES.add("Ljava/time/Duration;");
        SAFE_OBJECT_TYPES.add("Ljava/time/Instant;");
        SAFE_OBJECT_TYPES.add("Ljava/time/LocalDate;");
        SAFE_OBJECT_TYPES.add("Ljava/time/LocalDateTime;");
        SAFE_OBJECT_TYPES.add("Ljava/time/LocalTime;");
        SAFE_OBJECT_TYPES.add("Ljava/time/MonthDay;");
        SAFE_OBJECT_TYPES.add("Ljava/time/OffsetDateTime;");
        SAFE_OBJECT_TYPES.add("Ljava/time/OffsetTime;");
        SAFE_OBJECT_TYPES.add("Ljava/time/Period;");
        SAFE_OBJECT_TYPES.add("Ljava/time/Year;");
        SAFE_OBJECT_TYPES.add("Ljava/time/YearMonth;");
        SAFE_OBJECT_TYPES.add("Ljava/time/ZonedDateTime;");
        SAFE_OBJECT_TYPES.add("Ljava/time/ZonedId;");
        SAFE_OBJECT_TYPES.add("Ljava/time/ZoneOffset;");
        // these data types are not modified, when passed as a parameter to an unknown method
        IMMUTABLE_OBJECT_TYPES = new HashSet<String>(SAFE_OBJECT_TYPES.size() + 9);
        IMMUTABLE_OBJECT_TYPES.addAll(SAFE_OBJECT_TYPES);
        IMMUTABLE_OBJECT_TYPES.add("Ljava/lang/String;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/math/BigInteger;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/io/File;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/util/Locale;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/Inet4Address;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/Inet6Address;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/InetSocketAddress;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/URI;");
        IMMUTABLE_OBJECT_TYPES.add("Ljava/net/URL;");
        
        REPLACE_TAGS = new HashMap<String, Taint.Tag>();
        REPLACE_TAGS.put("\r", Taint.Tag.CR_ENCODED);
        REPLACE_TAGS.put("\n", Taint.Tag.LF_ENCODED);
        REPLACE_TAGS.put("\"", Taint.Tag.QUOTE_ENCODED);
        REPLACE_TAGS.put("'", Taint.Tag.APOSTROPHE_ENCODED);
        REPLACE_TAGS.put("<", Taint.Tag.LT_ENCODED);
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
        final Taint taint;
        if (SAFE_OBJECT_TYPES.contains(obj.getSignature(cpg))) {
            taint = new Taint(Taint.State.SAFE);
        } else {
            taint = new Taint(Taint.State.UNKNOWN);
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
        // keep the top of stack unchanged
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
        if (FindSecBugsGlobalConfig.getInstance().isDebugTaintState()) {
            taint.setDebugInfo(obj.getMethodName(cpg) + "()");
        }
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
            return getSummaryWithReplaceTags(summary, className, methodName);
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
    
    private TaintMethodSummary getSummaryWithReplaceTags(
            TaintMethodSummary summary, String className, String methodName) {
        if (!"java/lang/String".equals(className)) {
            return summary;
        }
        boolean isRegex = "replaceAll".equals(methodName);
        if (!isRegex && !"replace".equals(methodName)) {
            // not a replace method
            return summary;
        }
        try {
            String toReplace = getFrame().getStackValue(1).getConstantValue();
            if (toReplace == null) {
                // we don't know the exact value
                return summary;
            }
            Taint taint = summary.getOutputTaint();
            for (Map.Entry<String, Taint.Tag> replaceTag : REPLACE_TAGS.entrySet()) {
                String tagString = replaceTag.getKey();
                if ((isRegex && toReplace.contains(tagString))
                        || toReplace.equals(tagString)) {
                    taint.addTag(replaceTag.getValue());
                }
            }
            TaintMethodSummary summaryCopy = new TaintMethodSummary(summary);
            summaryCopy.setOuputTaint(taint);
            return summaryCopy;
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
    
    private TaintMethodSummary getSuperMethodSummary(String className, String methodId) {
        try {
            if (className.endsWith("]")) {
                // not a real class
                return null;
            }
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
        String className = methodDescriptor.getSlashedClassName();
        String methodId = "." + methodDescriptor.getName() + methodDescriptor.getSignature();
        if (analyzedMethodSummary.isInformative()
                || getSuperMethodSummary(className, methodId) != null) {
            String fullMethodName = className.concat(methodId);
            if (!methodSummaries.containsKey(fullMethodName)) {
                // prefer configured summaries to derived
                methodSummaries.put(fullMethodName, analyzedMethodSummary);
            }
        }
    }

    /**
     * For debugging purpose.
     * Print the state of the stack with information about the values in place.
     */
    private void printStackState() {
        try {
            System.out.println("============================");
            System.out.println("[[ Stack ]]");
            int stackDepth = getFrame().getStackDepth();
            for (int i = 0; i < stackDepth; i++) {
                Taint taintValue = getFrame().getStackValue(i);
                System.out.println(String.format("%s. %s {%s}",
                        i, taintValue.getState().toString(), taintValue.getDebugInfo()));
            }
            if (stackDepth == 0) {
                System.out.println("Empty");
            }
            System.out.println("============================");
        } catch (DataflowAnalysisException e) {
            System.out.println("Oups "+e.getMessage());
        }
    }
}
