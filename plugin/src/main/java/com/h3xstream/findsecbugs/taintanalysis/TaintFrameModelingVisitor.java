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
import java.util.ArrayList;
import java.util.Collection;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.NEW;

/**
 * Visitor to make instruction transfer of taint values easier
 *
 * @author David Formanek
 */
public class TaintFrameModelingVisitor extends AbstractFrameModelingVisitor<Taint, TaintFrame> {

    public TaintFrameModelingVisitor(ConstantPoolGen cpg) {
        super(cpg);
    }

    @Override
    public Taint getDefaultValue() {
        return Taint.UNKNOWN;
    }

    @Override
    public void visitLDC(LDC obj) {
        pushSafe();
    }

    @Override
    public void visitLDC2_W(LDC2_W obj) {
        pushSafe();
    }

    @Override
    public void visitACONST_NULL(ACONST_NULL obj) {
        getFrame().pushValue(Taint.NULL);
    }

    @Override
    public void visitNEW(NEW obj) {
        pushSafe();
    }
    
    @Override
    public void visitINVOKEVIRTUAL(INVOKEVIRTUAL obj) {
        String methodName = obj.getMethodName(cpg);
        String signature = obj.getSignature(cpg);
        String className = obj.getReferenceType(cpg).toString();
        String fullMethodName = className + "." + methodName + signature;
        System.out.println(fullMethodName);
        int numWordsConsumed = getNumWordsConsumed(obj);
        int numWordsProduced = getNumWordsProduced(obj);
        Collection<Integer> transferParameters = new ArrayList<Integer>();
        if (fullMethodName.equals("java.lang.StringBuilder.append(Ljava/lang/String;)Ljava/lang/StringBuilder;")) {
            System.out.println("append");
            transferParameters.add(0);
            transferParameters.add(1);
        }
        if ("toString".equals(methodName) && "()Ljava/lang/String;".equals(signature)) {
            System.out.println("toString");
            transferParameters.add(0);
        }
        Taint taint = null;
        for (Integer transferParameter : transferParameters) {
            try {
                Taint value = getFrame().getStackValue(transferParameter);
                System.out.println("transP " + transferParameter + ": " + value);
                taint = (taint == null) ? value : Taint.merge(taint, value);
            } catch (DataflowAnalysisException ex) {
                throw new RuntimeException("Bad transfer parameter specification", ex);
            }
        }
        if (taint == null) {
            taint = Taint.UNKNOWN;
        }
        System.out.println("final taint: " + taint);
        modelInstruction(obj, numWordsConsumed, numWordsProduced, taint);
    }

    private void pushSafe() {
        getFrame().pushValue(Taint.SAFE);
    }
}
