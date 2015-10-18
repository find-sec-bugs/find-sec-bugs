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
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.LDC;

public class RedirectionSource implements InjectionSource {

    private static final String UNVALIDATED_REDIRECT_TYPE = "UNVALIDATED_REDIRECT";

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
