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
package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.Method;

import java.util.Set;
import java.util.HashSet;

public class ModificationAfterValidationDetector extends OpcodeStackDetector {

    Set<OpcodeStack.Item> validated = new HashSet<OpcodeStack.Item>();

    private BugReporter bugReporter;

    public ModificationAfterValidationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visit(Method obj) {
        validated.clear();
        super.visit(obj);
    }

    private void reportBug() {
        BugInstance bug = new BugInstance(this, "MODIFICATION_AFTER_VALIDATION", Priorities.LOW_PRIORITY)
                .addClass(this).addMethod(this).addSourceLine(this);
        bugReporter.reportBug(bug);
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("java/util/regex/Pattern")
                && getNameConstantOperand().equals("matcher")) {
            validated.add(stack.getStackItem(0));
        } else if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("java/lang/String")
                       && getNameConstantOperand().startsWith("replace")) {
            if (validated.contains(stack.getItemMethodInvokedOn(this))) {
                reportBug();
            }
        }
    }
}
