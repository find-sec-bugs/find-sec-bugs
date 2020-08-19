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
package com.h3xstream.findsecbugs.groovy;

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class GroovyShellDetector extends OpcodeStackDetector {

    private static final String GROOVY_SHELL_TYPE = "GROOVY_SHELL";

    private BugReporter bugReporter;

    public GroovyShellDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("groovy/lang/GroovyShell")) {

            if(getNameConstantOperand().equals("evaluate") || getNameConstantOperand().equals("parse")) {
                bugReporter.reportBug(new BugInstance(this, GROOVY_SHELL_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }

        }
        else if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("groovy/lang/GroovyClassLoader")) {

            if(getNameConstantOperand().equals("parseClass")) {
                bugReporter.reportBug(new BugInstance(this, GROOVY_SHELL_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }

        }
    }
}
