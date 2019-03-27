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
package com.h3xstream.findsecbugs.jsp;

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class JspSpringEvalDetector extends OpcodeStackDetector {
    private static final String JSP_SPRING_EVAL = "JSP_SPRING_EVAL";

    private BugReporter bugReporter;

    public JspSpringEvalDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

//        JspSpringEvalDetector: [0039]  ldc   "${expression}"
//        JspSpringEvalDetector: [0041]  ldc   java/lang/String
//        JspSpringEvalDetector: [0043]  aload_2
//        JspSpringEvalDetector: [0044]  aconst_null
//        JspSpringEvalDetector: [0045]  invokestatic   org/apache/jasper/runtime/PageContextImpl.evaluateExpression (Ljava/lang/String;Ljava/lang/Class;Ljavax/servlet/jsp/PageContext;Lorg/apache/jasper/runtime/ProtectedFunctionMapper;)Ljava/lang/Object;
//        JspSpringEvalDetector: [0048]  checkcast
//        JspSpringEvalDetector: [0051]  invokevirtual   org/springframework/web/servlet/tags/EvalTag.setExpression (Ljava/lang/String;)V

        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("org/springframework/web/servlet/tags/EvalTag")
                && getNameConstantOperand().equals("setExpression") && getSigConstantOperand().equals("(Ljava/lang/String;)V")) {

            if (StackUtils.isVariableString(stack.getStackItem(0))) {
                bugReporter.reportBug(new BugInstance(this, JSP_SPRING_EVAL, Priorities.HIGH_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }
}
