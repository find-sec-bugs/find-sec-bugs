package com.h3xstream.findsecbugs.injection.script;

import com.h3xstream.findsecbugs.injection.InjectionSource;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.generic.*;

public class SpelSource implements InjectionSource {

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
    public int[] getInjectableParameters(InvokeInstruction ins, ConstantPoolGen cpg, InstructionHandle insHandle) {
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
                return new int[]{0};
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
                return new int[]{0};
            }
        }
        return new int[0];
    }
}
