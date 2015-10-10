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
package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InvokeInstruction;

public class ScriptEngineSource implements InjectionSource {

    private static final String SCRIPT_ENGINE_INJECTION_TYPE = "SCRIPT_ENGINE_INJECTION";

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //Signature capture from test class
        //invokeinterface   javax/script/ScriptEngine.eval (Ljava/lang/String;)Ljava/lang/Object;

        if (ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("javax.script.ScriptEngine") && methodName.equals("eval") &&
                    methodSignature.equals("(Ljava/lang/String;)Ljava/lang/Object;")) {
                return new InjectionPoint(new int[]{0},SCRIPT_ENGINE_INJECTION_TYPE);
            }
        }
        return InjectionPoint.NONE;
    }
}
