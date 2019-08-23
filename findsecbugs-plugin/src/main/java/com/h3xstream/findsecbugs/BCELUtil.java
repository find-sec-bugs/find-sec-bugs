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
package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.util.ClassName;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.generic.FieldOrMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InvokeInstruction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author Tomas Polesovsky
 */
public class BCELUtil {
    public static final String INVOKEDYNAMIC_GENERIC_CLASSNAME = Object.class.getName();

    /**
     * Caching class inheritance.
     */
    private static Map<String, Set<String>> superMap = new WeakHashMap<>();

    public static String getSlashedClassName(ConstantPoolGen cpg, FieldOrMethod obj) {
        if (Const.INVOKEDYNAMIC == obj.getOpcode()) {
            return INVOKEDYNAMIC_GENERIC_CLASSNAME;
        }

        ConstantPool cp = cpg.getConstantPool();
        ConstantCP invokeConstant = (ConstantCP)cp.getConstant(obj.getIndex());
        return cp.getConstantString(invokeConstant.getClassIndex(), Const.CONSTANT_Class);
    }

    public static String getSlashedClassName(JavaClass javaClass) {
        ConstantPool cp = javaClass.getConstantPool();
        int classNameIndex = javaClass.getClassNameIndex();
        return cp.getConstantString(classNameIndex, Const.CONSTANT_Class);
    }

    public static Set<String> getParentClassNames(JavaClass javaClass) {
        Set<String> classNames = superMap.get(javaClass.getClassName());

        if (classNames == null) {
            classNames = new HashSet<>();

            Queue<JavaClass> queue = new LinkedList<>();
            queue.offer(javaClass);
            while (!queue.isEmpty()) {
                JavaClass clazz = queue.poll();
                if (clazz != javaClass) {
                    classNames.add(BCELUtil.getSlashedClassName(clazz));
                }

                try {
                    JavaClass superClass = clazz.getSuperClass();
                    if (superClass != null) {
                        queue.offer(superClass);
                    }
                } catch (ClassNotFoundException ex) {
                    AnalysisContext.reportMissingClass(ex);

                    classNames.add(ClassName.toSlashedClassName(clazz.getSuperclassName()));
                }
                try {
                    for (JavaClass interfaceClass : clazz.getInterfaces()) {
                        queue.offer(interfaceClass);
                    }
                } catch (ClassNotFoundException ex) {
                    AnalysisContext.reportMissingClass(ex);

                    for (String interfaceClassName : clazz.getInterfaceNames()) {
                        classNames.add(ClassName.toSlashedClassName(interfaceClassName));
                    }
                }
            }

            superMap.put(javaClass.getClassName(), classNames);
        }

        return classNames;
    }

    public static int getNumArgumentsIncludingObjectInstance(InvokeInstruction obj, ConstantPoolGen cpg) {
        String signature = obj.getSignature(cpg);
        int count = 1;

        if ((obj.getOpcode() == Const.INVOKESTATIC) || (obj.getOpcode() == Const.INVOKEDYNAMIC)) {
            count = 0;
        }

        for (int i = 0; i < signature.length(); i++) {
            switch (signature.charAt(i)) {
                case '(':
                    break;

                case ')':
                    return count;

                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                    count++;
                    break;

                case 'L':
                    int semi = signature.indexOf(';', i + 1);
                    if (semi < 0) {
                        throw new IllegalStateException("Invalid method signature: " + signature);
                    }
                    i = semi;
                    count++;
                    break;

                case '[':
                    break;

                case 'V':
                default:
                    throw new IllegalStateException("Invalid method signature, unknown character " + signature.charAt(i) + " in " + signature);
            }
        }

        return count;
    }
}
