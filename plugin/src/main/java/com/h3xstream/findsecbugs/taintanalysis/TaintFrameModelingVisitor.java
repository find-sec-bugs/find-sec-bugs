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
import edu.umd.cs.findbugs.util.ClassName;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.bcel.Constants;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.ACONST_NULL;
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

/**
 * Visitor to make instruction transfer of taint values easier
 *
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class TaintFrameModelingVisitor extends AbstractFrameModelingVisitor<Taint, TaintFrame> {

    private static final String CONFIG_DIR = "taint-config";
    private static final String TRANSFER_METHODS_FILENAME = "transfer-methods.txt";
    private static final String TAINT_SOURCES_FILENAME = "taint-sources.txt";
    
    private static final String TO_STRING_METHOD = "toString()Ljava/lang/String;";
    private static final Collection<Integer> EMPTY_PARAMS = Collections.emptyList();
    private static final Collection<Integer> PARAM_0;
    private final Map<String, Collection<Integer>> transferMethods
            = new HashMap<String, Collection<Integer>>();
    private final Set<String> taintSources = new HashSet<String>();
    private final Map<String, Integer> transferMutables = new HashMap<String, Integer>();
    
    static {
        Collection<Integer> param0 = new ArrayList<Integer>(1);
        param0.add(0);
        PARAM_0 = Collections.unmodifiableCollection(param0);
    }
    
    public TaintFrameModelingVisitor(ConstantPoolGen cpg) {
        super(cpg);
        try {
            // separator is regex
            loadMaps(TRANSFER_METHODS_FILENAME, "\\|", "#");
            loadSet(TAINT_SOURCES_FILENAME, taintSources);
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        }
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
            Taint pushTaint = new Taint(arrayTaint.getState());
            modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), pushTaint);
        } catch (DataflowAnalysisException ex) {
            throw new InvalidBytecodeException("Not enough values on the stack", ex);
        }
    }
    
    private void visitInvoke(InvokeInstruction obj) {
        String methodNameWithSig = obj.getMethodName(cpg) + obj.getSignature(cpg);
        String fullMethodName = getSlashedClassName(obj) + "." + methodNameWithSig;
        Taint taint = getMethodTaint(methodNameWithSig, fullMethodName);
        transferTaintToMutables(fullMethodName, taint);
        modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), taint);
        transferTaintToStackTop(fullMethodName, taint);
    }

    private String getSlashedClassName(FieldOrMethod obj) {
        String className = obj.getReferenceType(cpg).toString();
        return ClassName.toSlashedClassName(className);
    }
    
    private Taint getMethodTaint(String methodNameWithSig, String fullMethodName) {
        if (taintSources.contains(fullMethodName)) {
            Taint taint = new Taint(Taint.State.TAINTED);
            taint.addTaintLocation(getLocation());
            return taint;
        }
        Collection<Integer> transferParameters;
        if (TO_STRING_METHOD.equals(methodNameWithSig)) {
            transferParameters = PARAM_0;
        } else {
            transferParameters = transferMethods.getOrDefault(fullMethodName, EMPTY_PARAMS);
        }
        return getMethodTaint(transferParameters);
    }
    
    private Taint getMethodTaint(Collection<Integer> transferParameters) {
        Taint taint = null;
        for (Integer transferParameter : transferParameters) {
            try {
                Taint value = getFrame().getStackValue(transferParameter);
                taint = (taint == null) ? value : Taint.merge(taint, value);
            } catch (DataflowAnalysisException ex) {
                throw new RuntimeException("Bad transfer parameter specification", ex);
            }
        }
        if (taint == null) {
            taint = getDefaultValue();
        }
        if (taint.getState() == Taint.State.UNKNOWN) {
            taint.addTaintLocation(getLocation(), false);
        }
        return taint;
    }
    
    private void transferTaintToMutables(String fullMethodName, Taint taint) throws RuntimeException {
        if (transferMutables.containsKey(fullMethodName)) {
            int mutableStackPosition = transferMutables.get(fullMethodName);
            try {
                Taint stackValue = getFrame().getStackValue(mutableStackPosition);
                if (stackValue.hasValidLocalVariableIndex()) {
                    int index = stackValue.getLocalVariableIndex();
                    getFrame().setValue(index, taint);
                }
                // else we are not able to transfer taint
            } catch (DataflowAnalysisException ex) {
                throw new RuntimeException("Bad mutable stack position specification", ex);
            }
        }
    }

    private void transferTaintToStackTop(String fullMethodName, Taint taint) {
        if (fullMethodName.contains("<init>")
                && transferMethods.containsKey(fullMethodName)
                && getFrame().getStackDepth() != 0) {
            try {
                Taint popValue = getFrame().popValue();
                getFrame().pushValue(Taint.merge(popValue, taint));
            } catch (DataflowAnalysisException ex) {
                assert false; // stack depth is not zero
            }
        }
    }
    
    private void pushSafe() {
        getFrame().pushValue(new Taint(Taint.State.SAFE));
    }
    
    private void loadMaps(String filename, String outerSeparator, String innerSparator)
            throws IOException {
        BufferedReader reader = null;
        try {
            reader = getReader(filename);
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                loadLineToMaps(line, outerSeparator, innerSparator);
            }
        } catch (NumberFormatException ex) {
            throw new IOException("Stack positions must be numbers", ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void loadLineToMaps(String line, String outerSeparator, String innerSparator)
            throws IOException, NumberFormatException {
        String[] outerTuple = line.split(outerSeparator);
        Integer mutablePosition = null;
        if (outerTuple.length > 2) {
            throw new IOException("More mutables not supported");
        } else if (outerTuple.length == 2) {
            mutablePosition = Integer.parseInt(outerTuple[1]);
        }
        String[] innerTuple = outerTuple[0].split(innerSparator);
        int count = innerTuple.length - 1;
        Collection<Integer> parameters = new ArrayList<Integer>(count);
        for (int i = 0; i < count; i++) {
            parameters.add(Integer.parseInt(innerTuple[i + 1]));
        }
        transferMethods.put(innerTuple[0], parameters);
        if (mutablePosition != null) {
            transferMutables.put(innerTuple[0], mutablePosition);
        }
    }

    private void loadSet(String filename, Set<String> set) throws IOException {
        BufferedReader reader = null;
        try {
            reader = getReader(filename);
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                set.add(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    private BufferedReader getReader(String filename) {
        String path = CONFIG_DIR + "/" + filename;
        return new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(path)
        ));
    }
}
