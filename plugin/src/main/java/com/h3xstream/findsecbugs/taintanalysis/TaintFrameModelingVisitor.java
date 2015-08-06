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
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.util.ClassName;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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

    private static final String METHODS_SUMMARIES_PATH = "taint-config/methods-summaries.txt";
    private static final String TO_STRING_METHOD = "toString()Ljava/lang/String;";
    private final TaintMethodSummaryMap methodSummaries = new TaintMethodSummaryMap();
    
    public TaintFrameModelingVisitor(ConstantPoolGen cpg) {
        super(cpg);
        InputStream stream = null;
        try {
            stream = getClass().getClassLoader().getResourceAsStream(METHODS_SUMMARIES_PATH);
            methodSummaries.load(stream);
        } catch (IOException ex) {
            throw new RuntimeException("cannot load resources", ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    throw new RuntimeException("cannot close stream", ex);
                }
            }
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
        TaintMethodSummary methodSummary = getMethodSummary(obj);
        Taint taint = getMethodTaint(methodSummary);
        if (taint.getState() == Taint.State.UNKNOWN) {
            taint.addTaintLocation(getLocation(), false);
        }
        transferTaintToMutables(methodSummary, taint);
        modelInstruction(obj, getNumWordsConsumed(obj), getNumWordsProduced(obj), taint);
    }

    private TaintMethodSummary getMethodSummary(InvokeInstruction obj) {
        String methodNameWithSig = obj.getMethodName(cpg) + obj.getSignature(cpg);
        String fullMethodName = getSlashedClassName(obj) + "." + methodNameWithSig;
        TaintMethodSummary methodSummary = methodSummaries.get(fullMethodName);
        if (methodSummary == null && TO_STRING_METHOD.equals(methodNameWithSig)) {
            methodSummary = TaintMethodSummary.getDefaultToStringSummary();
        }
        return methodSummary;
    }
    
    private String getSlashedClassName(FieldOrMethod obj) {
        String className = obj.getReferenceType(cpg).toString();
        return ClassName.toSlashedClassName(className);
    }
    
    private Taint getMethodTaint(TaintMethodSummary methodSummary) {
        if (methodSummary == null) {
            return getDefaultValue();
        }
        if (methodSummary.hasConstantOutputTaint()) {
            Taint taint = new Taint(methodSummary.getOutputTaint());
            if (taint.getState() == Taint.State.TAINTED) {
                taint.addTaintLocation(getLocation(), true);
            }
            return taint;
        }
        if (methodSummary.hasTransferParameters()) {
            return mergeTransferParameters(methodSummary.getTransferParameters());
        }
        throw new IllegalStateException("invalid method summary");
    }
    
    private Taint mergeTransferParameters(Collection<Integer> transferParameters) {
        Taint taint = null;
        assert !transferParameters.isEmpty();
        for (Integer transferParameter : transferParameters) {
            try {
                Taint value = getFrame().getStackValue(transferParameter);
                taint = (taint == null) ? value : Taint.merge(taint, value);
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
            for (Location location : taint.getTaintedLocations()) {
                stackValue.addTaintLocation(location, true);
            }
            for (Location location : taint.getPossibleTaintedLocations()) {
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
}
