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
package com.h3xstream.findsecbugs.template;

import com.h3xstream.findsecbugs.common.StackUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

/**
 * This detector does not use taint analysis because it does not make sense to use a template engine build from
 * constant. The vast majority of usage will read local files.
 */
public class VelocityDetector extends OpcodeStackDetector {

    private static final String VELOCITY_TYPE = "TEMPLATE_INJECTION_VELOCITY";

    private BugReporter bugReporter;

    public VelocityDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
//        printOpCode(seen);

        if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("org/apache/velocity/app/Velocity")
                && getNameConstantOperand().equals("evaluate")) {

            OpcodeStack.Item item = stack.getStackItem(0);
            if(!StackUtils.isConstantString(item)) {
                bugReporter.reportBug(new BugInstance(this, VELOCITY_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }
}
