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

import org.apache.bcel.generic.*;

import java.util.List;

public interface TaintFrameAdditionalVisitor {

    /**
     * @param invoke
     * @param cpg
     * @param methodGen Method
     * @param frameType Frame representation after the invoke (results)
     * @param parameters Stack representation just before the invoke
     */
    void visitInvoke(InvokeInstruction invoke, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters) throws Exception;

    /**
     *
     * @param invoke
     * @param cpg
     * @param methodGen Method
     * @param frameType Frame representation after the invoke (results)
     */
    void visitReturn(InvokeInstruction invoke, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType) throws Exception;


    /**
     * @param load
     * @param cpg
     * @param methodGen
     * @param frameType
     * @param numProduced
     */
    void visitLoad(LoadInstruction load, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType, int numProduced) throws Exception;


    /**
     * @param put
     * @param cpg
     * @param methodGen
     * @param frameType
     * @param numProduced
     */
    void visitField(FieldInstruction put, ConstantPoolGen cpg, MethodGen methodGen, TaintFrame frameType, int numProduced) throws Exception;


}
