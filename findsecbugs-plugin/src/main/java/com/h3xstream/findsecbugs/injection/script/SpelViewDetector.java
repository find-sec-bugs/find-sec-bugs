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
package com.h3xstream.findsecbugs.injection.script;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

import static org.apache.bcel.Const.INVOKESPECIAL;

/**
 * Detect a pattern that was found in multiple Spring components.
 *
 * https://gosecure.net/2018/05/15/beware-of-the-magic-spell-part-1-cve-2018-1273/
 * http://gosecure.net/2018/05/17/beware-of-the-magic-spell-part-2-cve-2018-1260/
 */
public class SpelViewDetector extends OpcodeStackDetector {

    private BugReporter bugReporter;

    public SpelViewDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);
        if(seen == INVOKESPECIAL) {
            String methodName = getNameConstantOperand();
            String className = getClassConstantOperand();

            if (methodName.equals("<init>") && className.toLowerCase().endsWith("spelview")) { //Constructor named SpelView()

                bugReporter.reportBug(new BugInstance(this, "SPEL_INJECTION", Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this).addString("SpelView()"));
            }
        }
    }
}
