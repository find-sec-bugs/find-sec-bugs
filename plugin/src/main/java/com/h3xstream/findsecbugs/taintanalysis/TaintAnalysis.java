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

import edu.umd.cs.findbugs.ba.BasicBlock;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.DepthFirstSearch;
import edu.umd.cs.findbugs.ba.Edge;
import edu.umd.cs.findbugs.ba.FrameDataflowAnalysis;
import edu.umd.cs.findbugs.ba.Location;
import edu.umd.cs.findbugs.ba.generic.GenericSignatureParser;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import java.util.Iterator;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

/**
 * Implements taint dataflow operations, in particular meeting facts, transfer
 * function is delegated to {@link TaintFrameModelingVisitor}
 * 
 * @author David Formanek
 */
public class TaintAnalysis extends FrameDataflowAnalysis<Taint, TaintFrame> {

    private final MethodGen methodGen;
    private final TaintFrameModelingVisitor visitor;
    private final int parameterStackSize;
    
    public TaintAnalysis(MethodGen methodGen, DepthFirstSearch dfs,
            MethodDescriptor descriptor, TaintMethodSummaryMap methodSummaries) {
        super(dfs);
        this.methodGen = methodGen;
        this.visitor = new TaintFrameModelingVisitor(
                methodGen.getConstantPool(), descriptor, methodSummaries);
        this.parameterStackSize = getParameterStackSize(
                descriptor.getSignature(), descriptor.isStatic());
    }

    @Override
    protected void mergeValues(TaintFrame frame, TaintFrame result, int i)
            throws DataflowAnalysisException {
        result.setValue(i, Taint.merge(result.getValue(i), frame.getValue(i)));
    }

    @Override
    public void transferInstruction(InstructionHandle handle, BasicBlock block, TaintFrame fact)
            throws DataflowAnalysisException {
        visitor.setFrameAndLocation(fact, new Location(handle, block));
        visitor.analyzeInstruction(handle.getInstruction());
    }

    @Override
    public TaintFrame createFact() {
        return new TaintFrame(methodGen.getMaxLocals());
    }

    @Override
    public void initEntryFact(TaintFrame fact) throws DataflowAnalysisException {
        fact.setValid();
        fact.clearStack();
        int numSlots = fact.getNumSlots();
        int numLocals = fact.getNumLocals();
        for (int i = 0; i < numSlots; ++i) {
            Taint value = new Taint(Taint.State.UNKNOWN);
            if (i < numLocals) {
                value.setVariableIndex(i);
                if (i < parameterStackSize) {
                    int stackOffset = parameterStackSize - i - 1;
                    value.addParameter(stackOffset);
                }
            }
            fact.setValue(i, value);
        }
    }

    @Override
    public void meetInto(TaintFrame fact, Edge edge, TaintFrame result)
            throws DataflowAnalysisException {
        if (fact.isValid() && edge.isExceptionEdge()) {
            TaintFrame copy = null;
            // creates modifiable copy
            copy = modifyFrame(fact, copy);
            copy.clearStack();
            // do not trust values that are safe just when an exception occurs
            copy.pushValue(new Taint(Taint.State.UNKNOWN));
            fact = copy;
        }
        mergeInto(fact, result);
    }
    
    public void finishAnalysis() {
        visitor.finishAnalysis();
    }
    
    private static int getParameterStackSize(String signature, boolean isStatic) {
        assert signature != null && !signature.isEmpty();
        // static methods does not have reference to this
        int stackSize = isStatic ? 0 : 1;
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
}
