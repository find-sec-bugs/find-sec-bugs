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
package com.h3xstream.findsecbugs.injection.redirect;

import com.h3xstream.findsecbugs.common.ByteCode;
import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.*;

public class RedirectionSource implements InjectionSource {

    private static final String UNVALIDATED_REDIRECT_TYPE = "UNVALIDATED_REDIRECT";

    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        for (int i = 0; i < cpg.getSize(); i++) {
            Constant cnt = cpg.getConstant(i);
            if (cnt instanceof ConstantUtf8) {
                String utf8String = ((ConstantUtf8) cnt).getBytes();
//                System.out.println("cnt= "+utf8String);
                if (utf8String.equals("javax/servlet/http/HttpServletResponse")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //ByteCode.printOpCode(ins, cpg);

        if (ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("javax.servlet.http.HttpServletResponse")) {
                if (methodName.equals("sendRedirect")) {
                    return new InjectionPoint(new int[]{0},UNVALIDATED_REDIRECT_TYPE);
                } else if (methodName.equals("addHeader")) {

                    LDC ldc = ByteCode.getPrevInstruction(insHandle, LDC.class);
                    if (ldc != null) {
                        Object value = ldc.getValue(cpg);
                        if ("Location".equals(value)) {
                            return new InjectionPoint(new int[]{0},UNVALIDATED_REDIRECT_TYPE);
                        }
                    }

                }
            }
        }
        return InjectionPoint.NONE;
    }
}
