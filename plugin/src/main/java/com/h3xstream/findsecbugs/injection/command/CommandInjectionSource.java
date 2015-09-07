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
package com.h3xstream.findsecbugs.injection.command;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
public class CommandInjectionSource implements InjectionSource {

    private static final String COMMAND_INJECTION_TYPE = "COMMAND_INJECTION";


    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        for (int i = 0; i < cpg.getSize(); i++) {
            Constant cnt = cpg.getConstant(i);
            if (cnt instanceof ConstantUtf8) {
                String utf8String = ((ConstantUtf8) cnt).getBytes();
                if (utf8String.startsWith("java/lang/Runtime")) {
                    return true;
                } else if (utf8String.startsWith("java/lang/ProcessBuilder")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {

        //ByteCode.printOpCode(ins,cpg);

        if (ins instanceof INVOKEVIRTUAL) {
            String methodName = ins.getMethodName(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("java.lang.Runtime") && methodName.equals("exec")) {
                InjectionPoint ip = new InjectionPoint(new int[]{0}, COMMAND_INJECTION_TYPE);
                ip.setInjectableMethod("Runtime.exec(...)");
                return ip;
            } else if (className.equals("java.lang.ProcessBuilder") && methodName.equals("command")) {
                //INVOKEVIRTUAL java/lang/ProcessBuilder.command([Ljava/lang/String;)Ljava/lang/ProcessBuilder;
                //INVOKEVIRTUAL java/lang/ProcessBuilder.command(Ljava/util/List;)Ljava/lang/ProcessBuilder;

                InjectionPoint ip = new InjectionPoint(new int[]{0}, COMMAND_INJECTION_TYPE);
                ip.setInjectableMethod("ProcessBuilder.command(...)");
                return ip;
            }
        }
        else if(ins instanceof INVOKESPECIAL) {
            String methodName = ins.getMethodName(cpg);
            String className = ins.getClassName(cpg);
            if (className.equals("java.lang.ProcessBuilder") && methodName.equals("<init>")) {
                //INVOKESPECIAL java/lang/ProcessBuilder.<init>([Ljava/lang/String;)V
                //INVOKESPECIAL java/lang/ProcessBuilder.<init>(Ljava/util/List;)V

                InjectionPoint ip = new InjectionPoint(new int[]{0}, COMMAND_INJECTION_TYPE);
                ip.setInjectableMethod("ProcessBuilder(...)");
                return ip;
            }
        }
        return InjectionPoint.NONE;
    }
}
