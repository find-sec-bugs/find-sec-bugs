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

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.DataflowAnalysisException;
import edu.umd.cs.findbugs.ba.Location;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.ObjectType;

public class DeserializationGadgetDetector implements Detector {
    
    private static final String DESERIALIZATION_GADGET_TYPE = "DESERIALIZATION_GADGET";
    private static final List<String> DANGEROUS_APIS = Arrays.asList(
            "java/lang/reflect/Method", //
            "java/lang/reflect/Constructor", //
            "org/springframework/beans/BeanUtils", //
            "org/apache/commons/beanutils/BeanUtils", //
            "org/apache/commons/beanutils/PropertyUtils", //
            "org/springframework/util/ReflectionUtils");
    List<String> classesToIgnoreInReadObjectMethod = Arrays.asList("java/io/ObjectInputStream","java/lang/Object");
    private final BugReporter bugReporter;
    private static final List<String> READ_DESERIALIZATION_METHODS = Arrays.asList("readObject", //
            "readUnshared", "readArray", "readResolve");

    public DeserializationGadgetDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();
        boolean isSerializable = InterfaceUtils.isSubtype(javaClass, "java.io.Serializable");
        boolean isInvocationHandler = InterfaceUtils.isSubtype(javaClass, "java.lang.reflect.InvocationHandler");
        boolean useDangerousApis = false;
        boolean customReadObjectMethod = false;
        boolean customInvokeMethod = false;
        boolean hasMethodField = false;
        if (!isSerializable) {
            return; //Nothing to see, move on.
        }

        for (Constant c : javaClass.getConstantPool().getConstantPool()) {
            if (c instanceof ConstantUtf8) {
                ConstantUtf8 utf8 = (ConstantUtf8) c;
                String constantValue = String.valueOf(utf8.getBytes());
                //System.out.println(constantValue);
                if (DANGEROUS_APIS.contains(constantValue)) {
                    useDangerousApis = true;
                    break;
                }
            }
        }

        for (Method m : javaClass.getMethods()) {
            if (!customReadObjectMethod && READ_DESERIALIZATION_METHODS.contains(m.getName())) {
                try {
                    customReadObjectMethod = hasCustomReadObject(m, classContext, classesToIgnoreInReadObjectMethod);
                } catch (CFGBuilderException | DataflowAnalysisException e) {
                    AnalysisContext.logError("Cannot check custom read object", e);
                }
            } else if (!customInvokeMethod && "invoke".equals(m.getName())) {
                try {
                    customInvokeMethod = hasCustomReadObject(m, classContext, classesToIgnoreInReadObjectMethod);
                } catch (CFGBuilderException | DataflowAnalysisException e) {
                    AnalysisContext.logError("Cannot check custom read object", e);
                }
            }
        }

        for (Field f : javaClass.getFields()) {
            if ((f.getName().toLowerCase().contains("method") && f.getType().equals(new ObjectType("java.lang.String")))
                    || f.getType().equals(new ObjectType("java.reflect.Method"))) {
                hasMethodField = true;
            }
        }

        if ((isSerializable && customReadObjectMethod) || (isInvocationHandler && customInvokeMethod)) {
            int priority = useDangerousApis ? Priorities.NORMAL_PRIORITY : Priorities.LOW_PRIORITY;
            bugReporter.reportBug(new BugInstance(this, DESERIALIZATION_GADGET_TYPE, priority) //
                    .addClass(javaClass));
        } else if (isSerializable && hasMethodField && useDangerousApis) {
            bugReporter.reportBug(new BugInstance(this, DESERIALIZATION_GADGET_TYPE, Priorities.LOW_PRIORITY) //
                    .addClass(javaClass));
        }
    }

    /**
     * Check if the readObject is doing multiple external call beyond the basic readByte, readBoolean, etc..
     * @param m
     * @param classContext
     * @return
     * @throws CFGBuilderException
     * @throws DataflowAnalysisException
     */
    private boolean hasCustomReadObject(Method m, ClassContext classContext,List<String> classesToIgnore)
            throws CFGBuilderException, DataflowAnalysisException {
        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);
        int count = 0;
        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();
            Instruction inst = location.getHandle().getInstruction();
            //ByteCode.printOpCode(inst,cpg);
            if(inst instanceof InvokeInstruction) {
                InvokeInstruction invoke = (InvokeInstruction) inst;
                if (!READ_DESERIALIZATION_METHODS.contains(invoke.getMethodName(cpg))
                        && !classesToIgnore.contains(invoke.getClassName(cpg))) {
                    count +=1;
                }
            }
        }
        return count > 3;
    }

    @Override
    public void report() {
    }
}
