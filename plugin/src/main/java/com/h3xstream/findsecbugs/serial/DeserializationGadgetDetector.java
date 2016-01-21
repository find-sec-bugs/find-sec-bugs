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
package com.h3xstream.findsecbugs.serial;

import com.h3xstream.findsecbugs.ObjectDeserializationDetector;
import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantMethodHandle;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import java.util.Arrays;
import java.util.List;

public class DeserializationGadgetDetector implements Detector {
    private static final String DESERIALIZATION_GADGET_TYPE = "DESERIALIZATION_GADGET";

    private static final List<String> DANGEROUS_APIS = Arrays.asList(
            "java/lang/reflect/Method", //
            "java/lang/reflect/Constructor", //
            "org/springframework/beans/BeanUtils", //
            "org/apache/commons/beanutils/BeanUtils",
            "org/apache/commons/beanutils/PropertyUtils");

    private BugReporter bugReporter;

    private static List<String> READ_DESERIALIZATION_METHODS = Arrays.asList("readObject", //
            "readUnshared", "readArray", "readResolve");

    public DeserializationGadgetDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        boolean isSerializable = InterfaceUtils.isSubtype(javaClass, "java.io.Serializable");
        boolean useDangerousApis = false;
        boolean hasReadObjetMethod = false;
        if(!isSerializable) return; //Nothing to see, move on.


        dangerousApis: for(Constant c : javaClass.getConstantPool().getConstantPool()) {
            if (c instanceof ConstantUtf8) {
                ConstantUtf8 utf8 = (ConstantUtf8) c;
                String constantValue = String.valueOf(utf8.getBytes());
                //System.out.println(constantValue);
                if (DANGEROUS_APIS.contains(constantValue)) {
                    useDangerousApis = true;
                    break dangerousApis;
                }
            }

        }

        readObjet : for (Method m : javaClass.getMethods()) {
            if(READ_DESERIALIZATION_METHODS.contains(m.getName())) {
                hasReadObjetMethod = true;
                break readObjet;
            }
        }

        if(isSerializable && useDangerousApis) {
            int priority = hasReadObjetMethod ? Priorities.NORMAL_PRIORITY : Priorities.LOW_PRIORITY;

            bugReporter.reportBug(new BugInstance(this, DESERIALIZATION_GADGET_TYPE, priority) //
                    .addClass(javaClass));
        }

    }

    @Override
    public void report() {

    }
}
