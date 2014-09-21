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
package com.h3xstream.findsecbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Constants;

public class PredictableRandomDetector extends OpcodeStackDetector {

    private static final String PREDICTABLE_RANDOM_TYPE = "PREDICTABLE_RANDOM";

    private BugReporter bugReporter;

    public PredictableRandomDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Constants.INVOKESPECIAL && getClassConstantOperand().equals("java/util/Random")
                && getNameConstantOperand().equals("<init>")) {
            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.util.Random"));

        } else if (seen == Constants.INVOKESPECIAL && getClassConstantOperand().equals("scala/util/Random")
                && getNameConstantOperand().equals("<init>")) {
            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("scala.util.Random"));

        } else if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("java/lang/Math")
                && getNameConstantOperand().equals("random")) {
            bugReporter.reportBug(new BugInstance(this, PREDICTABLE_RANDOM_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this) //
                    .addString("java.lang.Math.random()"));
        }
    }

}
