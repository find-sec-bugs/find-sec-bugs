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
package com.h3xstream.findsecbugs.common;

import edu.umd.cs.findbugs.OpcodeStack;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;

public class StackUtils {

    public static boolean isConstantString(OpcodeStack.Item item) {
        return item.getConstant() != null && item.getConstant() instanceof String;
    }

    /**
     * @param item Stack item (parameter passed to the current function)
     * @return If the given string reference is not a constant
     */
    public static boolean isVariableString(OpcodeStack.Item item) {
        return !isConstantString(item);
    }

    public static boolean isConstantInteger(OpcodeStack.Item item) {
        return item.getConstant() != null && item.getConstant() instanceof Integer;
    }

    public static LocalVariableGen getLocalVariable(MethodGen methodGen, int index) {
        for(LocalVariableGen local : methodGen.getLocalVariables()) {
            if(local.getIndex() == index) {
                return local;
            }
        }
        return null;
    }

}
