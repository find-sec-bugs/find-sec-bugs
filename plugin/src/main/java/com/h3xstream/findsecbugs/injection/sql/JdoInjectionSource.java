/**
 * Find Security Bugs
 * Copyright (c) 2014, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.injection.InjectionSource;

import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * API reference : http://db.apache.org/jdo/index.html
 */
public class JdoInjectionSource implements InjectionSource {

    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        for (int i = 0; i < cpg.getSize(); i++) {
            Constant cnt = cpg.getConstant(i);
            if (cnt instanceof ConstantUtf8) {
                String utf8String = ((ConstantUtf8) cnt).getBytes();
                //System.out.println("cnt= "+utf8String);
                if (utf8String.equals("Ljavax/jdo/PersistenceManager;")) {
                    return true;
                } else if (utf8String.equals("Ljavax/jdo/Query;")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int[] getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //ByteCode.printOpCode(ins, cpg);

        if (ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new int[]{0};
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/String;Ljava/lang/Object;)Ljavax/jdo/Query;")) {
                return new int[]{0};
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/Class;Ljava/util/Collection;Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new int[]{0};
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/Class;Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new int[]{0};
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljavax/jdo/Extent;Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new int[]{0};
            } else if (className.equals("javax.jdo.Query") && methodName.equals("setFilter") &&
                    methodSignature.equals("(Ljava/lang/String;)V")) {
                return new int[]{0};
            } else if (className.equals("javax.jdo.Query") && methodName.equals("setGrouping") &&
                    methodSignature.equals("(Ljava/lang/String;)V")) {
                return new int[]{0};
            }
        }
        return new int[0];
    }


}
