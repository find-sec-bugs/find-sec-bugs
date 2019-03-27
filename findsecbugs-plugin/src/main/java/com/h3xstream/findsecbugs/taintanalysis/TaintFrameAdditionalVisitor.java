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
     * This method will be triggered for every method invocation (static, interface, special and virtual).
     * The constant pool allowed the resolution of method name, field name, constant strings, etc.
     *
     * The taintframe
     * @param invoke
     * @param methodGen Method
     * @param frameType Frame representation after the invoke (results)
     * @param parameters Stack representation just before the invoke
     * @param cpg
     */
    void visitInvoke(InvokeInstruction invoke, MethodGen methodGen, TaintFrame frameType, List<Taint> parameters, ConstantPoolGen cpg) throws Exception;

    /**
     *
     * @param methodGen Method
     * @param returnValue State of the returned value.
     * @param cpg
     */
    void visitReturn(MethodGen methodGen, Taint returnValue, ConstantPoolGen cpg) throws Exception;


    /**
     * @param load
     * @param methodGen
     * @param frameType
     * @param numProduced
     * @param cpg
     */
    void visitLoad(LoadInstruction load, MethodGen methodGen, TaintFrame frameType, int numProduced, ConstantPoolGen cpg) throws Exception;


    /**
     * @param put
     * @param methodGen
     * @param frameType
     * @param taintFrame
     * @param numProduced
     * @param cpg
     */
    void visitField(FieldInstruction put, MethodGen methodGen, TaintFrame frameType,Taint taintFrame, int numProduced, ConstantPoolGen cpg) throws Exception;


}
