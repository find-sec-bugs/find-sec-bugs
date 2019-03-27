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
package com.h3xstream.findsecbugs.injection;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

public interface InjectionSource {

    /**
     * The implementation should identify method that are susceptible to injection and return
     * parameters index that can injected.
     *
     * @param ins       Instruction visit
     * @param cpg       ConstantPool (needed to find the class name and method name associate to instruction)
     * @param insHandle instruction handle (needed to look at the instruction around the current instruction)
     * @return Precision about the parameter at risk for the current instruction visit. (InjectionPoint.NONE when the method is safe)
     */
    InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle);

}
