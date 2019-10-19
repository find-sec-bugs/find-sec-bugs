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

import org.apache.bcel.Const;

import com.mitchellbosecke.pebble.template.PebbleTemplate;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class PebbleDetector extends OpcodeStackDetector {
    private static final String PEBBLE_TYPE = "TEMPLATE_INJECTION_PEBBLE";

    private BugReporter bugReporter;

    public PebbleDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("com/mitchellbosecke/pebble/template/PebbleTemplate")
                && getNameConstantOperand().equals("evaluate")) {

            bugReporter.reportBug(new BugInstance(this, PEBBLE_TYPE, Priorities.NORMAL_PRIORITY)
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
    }
}