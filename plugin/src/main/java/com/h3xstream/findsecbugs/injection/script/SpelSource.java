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
package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findsecbugs.injection.InjectionPoint;
import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.*;

public class SpelSource implements InjectionSource {

    private static final String SPEL_INJECTION_TYPE = "SPEL_INJECTION";


    @Override
    public boolean isCandidate(ConstantPoolGen cpg) {
        for (int i = 0; i < cpg.getSize(); i++) {
            Constant cnt = cpg.getConstant(i);
            if (cnt instanceof ConstantUtf8) {
                String utf8String = ((ConstantUtf8) cnt).getBytes();
                if (utf8String.startsWith("org/springframework/expression")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public InjectionPoint getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
        //Signature capture from test class
        //invokeinterface   org/springframework/expression/ExpressionParser.parseExpression (Ljava/lang/String;)Lorg/springframework/expression/Expression;
        //invokevirtual   org/springframework/expression/spel/standard/SpelExpressionParser.parseExpression (Ljava/lang/String;)Lorg/springframework/expression/Expression;
        //invokevirtual   org/springframework/expression/common/TemplateAwareExpressionParser.parseExpression (Ljava/lang/String;)Lorg/springframework/expression/Expression;

        if (ins instanceof INVOKEINTERFACE) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            if (className.equals("org.springframework.expression.ExpressionParser") && methodName.equals("parseExpression") &&
                    methodSignature.equals("(Ljava/lang/String;)Lorg/springframework/expression/Expression;")) {
                return new InjectionPoint(new int[]{0},SPEL_INJECTION_TYPE);
            }
        }
        else if(ins instanceof INVOKEVIRTUAL) {
            String methodName = ins.getMethodName(cpg);
            String methodSignature = ins.getSignature(cpg);
            String className = ins.getClassName(cpg);

            if ((className.equals("org.springframework.expression.spel.standard.SpelExpressionParser") ||
                    className.equals("org.springframework.expression.common.TemplateAwareExpressionParser"))
                    && methodName.equals("parseExpression") &&
                    methodSignature.equals("(Ljava/lang/String;)Lorg/springframework/expression/Expression;")) {
                return new InjectionPoint(new int[]{0},SPEL_INJECTION_TYPE);
            }
        }
        return InjectionPoint.NONE;
    }


}
