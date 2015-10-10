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
package com.h3xstream.findsecbugs.injection.sql;

import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

/**
 * API reference : http://db.apache.org/jdo/index.html
 */
public class JdoInjectionSource implements InjectionSource {

    protected static final String SQL_INJECTION_TYPE = "SQL_INJECTION_JDO";

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //ByteCode.printOpCode(ins, cpg);

        if (ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/String;Ljava/lang/Object;)Ljavax/jdo/Query;")) {
                return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/Class;Ljava/util/Collection;Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljava/lang/Class;Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
            } else if (className.equals("javax.jdo.PersistenceManager") && methodName.equals("newQuery") &&
                    methodSignature.equals("(Ljavax/jdo/Extent;Ljava/lang/String;)Ljavax/jdo/Query;")) {
                return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
            } else if (className.equals("javax.jdo.Query") && methodName.equals("setFilter") &&
                    methodSignature.equals("(Ljava/lang/String;)V")) {
                return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
            } else if (className.equals("javax.jdo.Query") && methodName.equals("setGrouping") &&
                    methodSignature.equals("(Ljava/lang/String;)V")) {
                return new InjectionPoint(new int[]{0}, SQL_INJECTION_TYPE);
            }
        }
        return InjectionPoint.NONE;
    }


}
