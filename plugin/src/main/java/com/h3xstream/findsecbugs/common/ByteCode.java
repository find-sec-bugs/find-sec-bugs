/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs.common;

import org.apache.bcel.generic.*;

public class ByteCode {

    /**
     * Print the the detail of the given instruction (class, method, etc.)
     *
     * @param ins Instruction
     * @param cpg Constant Pool
     */
    public static void printOpCode(Instruction ins, ConstantPoolGen cpg) {
        if (ins instanceof InvokeInstruction) {
            InvokeInstruction invokeIns = (InvokeInstruction) ins;
            System.out.println(ins.getClass().getSimpleName() + " " + invokeIns.getClassName(cpg).replaceAll("\\.", "/") + "." + invokeIns.getMethodName(cpg) + " (" + invokeIns.getSignature(cpg) + ")");
        } else {
            System.out.println(ins.getClass().getSimpleName() + " " + ins.toString());
        }
    }

    /**
     * Get the constant value of the given instruction.
     * (The instruction must refer to the Constant Pool otherwise null is return)
     * @param h
     * @param cpg
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T  getConstantLDC(InstructionHandle h, ConstantPoolGen cpg, Class<T> clazz) {
        Instruction prevIns = h.getInstruction();
        if(prevIns instanceof LDC) {
            LDC ldcCipher = (LDC) prevIns;
            Object val = ldcCipher.getValue(cpg);
            if(val.getClass().equals(clazz)) {
                return clazz.cast(val);
            }
        }

        return null;
    }

    /**
     * Extract the number from a push operation (BIPUSH/SIPUSH).
     * @param h
     * @return
     */
    public static Number getPushNumber(InstructionHandle h) {
        Instruction prevIns = h.getInstruction();
        if(prevIns instanceof BIPUSH) {
            BIPUSH ldcCipher = (BIPUSH) prevIns;
            return ldcCipher.getValue();
        }
        else if(prevIns instanceof SIPUSH) {
            SIPUSH ldcCipher = (SIPUSH) prevIns;
            return ldcCipher.getValue();
        }
        return null;
    }

    /**
     * Get the previous instruction matching the given type of instruction (second parameter)
     * @param startHandle
     * @param clazz Type of instruction to look for
     * @param <T>
     * @return
     */
    public static <T> T getPrevInstruction(InstructionHandle startHandle,Class<T> clazz) {
        InstructionHandle curHandle = startHandle;
        while(curHandle != null) {
            curHandle = curHandle.getPrev();

            if(curHandle != null && clazz.isInstance(curHandle.getInstruction())) {
                return clazz.cast(curHandle.getInstruction());
            }
        }
        return null;
    }
}
