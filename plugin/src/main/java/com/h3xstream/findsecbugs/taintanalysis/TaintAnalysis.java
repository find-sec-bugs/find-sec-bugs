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
    
    public TaintAnalysis(MethodGen methodGen, DepthFirstSearch dfs) {
        super(dfs);
        this.methodGen = methodGen;
        this.visitor = new TaintFrameModelingVisitor(methodGen.getConstantPool());
    }

    @Override
    protected void mergeValues(TaintFrame frame, TaintFrame result, int i) throws DataflowAnalysisException {
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
        for (int i = 0; i < numSlots; ++i) {
            fact.setValue(i, new Taint(Taint.State.UNKNOWN));
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
}
