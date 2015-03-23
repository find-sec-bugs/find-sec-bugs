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
package com.h3xstream.findsecbugs.xpath;

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class XPathInjectionJavaxDetector extends OpcodeStackDetector {

    private static final String XPATH_INJECTION_TYPE = "XPATH_INJECTION";

    private BugReporter bugReporter;

    public XPathInjectionJavaxDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKEINTERFACE && getClassConstantOperand().equals("javax/xml/xpath/XPath")) {

            if (getNameConstantOperand().equals("compile")
                    && getSigConstantOperand().equals("(Ljava/lang/String;)Ljavax/xml/xpath/XPathExpression;")) {

                if (StackUtils.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, XPATH_INJECTION_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("XPath.compile()"));
                }
            } else if (getNameConstantOperand().equals("evaluate")
                    && getSigConstantOperand().equals("(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;")) {

                if (StackUtils.isVariableString(stack.getStackItem(0))) {
                    bugReporter.reportBug(new BugInstance(this, XPATH_INJECTION_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("XPath.evaluate()"));
                }
            }
        }
    }

}
