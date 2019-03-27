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

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LDC;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.*;

public class JspUtils {


    /**
     *
     * @param precedingInstructions Instructions preceding the call.
     * @param cpg Constant Pool Gen (need to get constant value such as strings and class names)
     * @return The string value found or null
     */
    public static String getContanstBooleanAsString(LinkedList<Instruction> precedingInstructions, ConstantPoolGen cpg) {
        //Heuristic to find static
        Iterator<Instruction> it = precedingInstructions.descendingIterator();

// Weblogic 12
//     [0076]  ldc_w   "true"
//     [0079]  invokestatic   weblogic/utils/StringUtils.valueOf (Ljava/lang/Object;)Ljava/lang/String;
//     [0082]  invokestatic   java/lang/Boolean.valueOf (Ljava/lang/String;)Ljava/lang/Boolean;
//     [0085]  invokevirtual   java/lang/Boolean.booleanValue ()Z
//     [0088]  invokevirtual   org/apache/taglibs/standard/tag/rt/core/OutTag.setEscapeXml (Z)V

        try {
            it.next();
            INVOKEVIRTUAL inv1 = (INVOKEVIRTUAL) it.next();
            INVOKESTATIC inv2 = (INVOKESTATIC) it.next();
            INVOKESTATIC inv3 = (INVOKESTATIC) it.next();
            if (invokeInstruction().atClass("java.lang.Boolean").atMethod("booleanValue").matches(inv1, cpg) &&
                    invokeInstruction().atClass("java.lang.Boolean").atMethod("valueOf").matches(inv2, cpg) &&
                    invokeInstruction().atClass("weblogic.utils.StringUtils").atMethod("valueOf").matches(inv3, cpg)) {
                LDC ldc = (LDC) it.next();
                return String.valueOf(ldc.getValue(cpg));
            }
        }
        catch (NoSuchElementException e) { }
        catch (ClassCastException e) { }
/**
// WebLogic Unknown
//     [0076]  ldc_w   "false"
//     [0079]  ldc   java/lang/String
//     [0081]  ldc_w   "escapeXml"
//     [0084]  invokestatic   weblogic/jsp/internal/jsp/utils/JspRuntimeUtils.convertType (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;
//     [0087]  checkcast
//     [0090]  invokevirtual   org/apache/taglibs/standard/tag/el/core/OutTag.setEscapeXml (Ljava/lang/String;)V

        it = precedingInstructions.descendingIterator();

        try {
            it.next();
            CHECKCAST checkcast = (CHECKCAST) it.next();
            INVOKESTATIC inv1 = (INVOKESTATIC) it.next();
            if (invokeInstruction().atClass("weblogic.jsp.internal.jsp.utils.JspRuntimeUtils").atMethod("convertType").matches(inv1, cpg)) {
                LDC ldc1 = (LDC) it.next(); //property name
                LDC ldc2 = (LDC) it.next(); //type
                LDC ldc3 = (LDC) it.next(); //value
                return String.valueOf(ldc3.getValue(cpg));
            }
        }
        catch (NoSuchElementException e) { }
        catch (ClassCastException e) { }
*/
        return null;
    }
}
