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
package com.h3xstream.findsecbugs.xpath;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class XPathInjectionApacheXPathApiDetector extends OpcodeStackDetector {

    private static final String XPATH_INJECTION_TYPE = "XPATH_INJECTION";

    private BugReporter bugReporter;

    public XPathInjectionApacheXPathApiDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("org/apache/xpath/XPathAPI")) {
            if (getNameConstantOperand().equals("selectNodeIterator") ||
                    getNameConstantOperand().equals("selectNodeList") ||
                    getNameConstantOperand().equals("selectSingleNode")) {

                //For all 3 methos the query is in the last parameter
                if (StringTracer.isVariableString(stack.getStackItem(0))) {

                    bugReporter.reportBug(new BugInstance(this, XPATH_INJECTION_TYPE, Priorities.NORMAL_PRIORITY) //
                            .addClass(this).addMethod(this).addSourceLine(this) //
                            .addString("XPathApi." + getNameConstantOperand() + "()"));
                }
            }
        }
    }
}
