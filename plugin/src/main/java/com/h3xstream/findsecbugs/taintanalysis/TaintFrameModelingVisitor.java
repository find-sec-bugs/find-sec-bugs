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
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;

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
    
    private void pushSafe() {
        getFrame().pushValue(Taint.SAFE);
    }
}
