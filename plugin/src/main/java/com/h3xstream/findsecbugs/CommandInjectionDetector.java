/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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
package com.h3xstream.findsecbugs;

import com.h3xstream.findsecbugs.common.StringTracer;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

/**
 * Detect the usage of Runtime and ProcessBuilder to execute system command.
 *
 * @see java.lang.ProcessBuilder
 * @see java.lang.Runtime
 */
public class CommandInjectionDetector extends OpcodeStackDetector {

    private static final String COMMAND_INJECTION_TYPE = "COMMAND_INJECTION";

    private BugReporter bugReporter;

    public CommandInjectionDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/Runtime") &&
                getNameConstantOperand().equals("exec")) {

            if (StringTracer.isVariableString(stack.getStackItem(0))) {
                bugReporter.reportBug(new BugInstance(this, COMMAND_INJECTION_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString("Runtime.exec(...)"));
            }
        } else if (seen == Constants.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/ProcessBuilder") &&
                getNameConstantOperand().equals("command")) {
            if (StringTracer.hasVariableString(stack)) {
                bugReporter.reportBug(new BugInstance(this, COMMAND_INJECTION_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this)
                        .addString("ProcessBuilder.command(...)"));
            }
        }

    }
}
