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

import java.util.Set;
import java.util.HashSet;

public class NormalizeBeforeValidationDetector extends OpcodeStackDetector {

    Set<OpcodeStack.Item> Validated = new HashSet<OpcodeStack.Item>();

    private BugReporter bugReporter;

    public NormalizeBeforeValidationDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    private void reportBug() {
        BugInstance bug = new BugInstance(this, "NORMALIZE_BEFORE_VALIDATION", Priorities.LOW_PRIORITY)
                .addClass(this).addMethod(this).addSourceLine(this);
        bugReporter.reportBug(bug);
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("java/util/regex/Pattern")
                && getNameConstantOperand().equals("matcher")) {
            Validated.add(stack.getStackItem(0));
        } else if (seen == Const.INVOKESTATIC && getClassConstantOperand().equals("java/text/Normalizer")
                       && getNameConstantOperand().equals("normalize")) {
            if (Validated.contains(stack.getStackItem(1))) {
                reportBug();
            }
        }
    }
}
