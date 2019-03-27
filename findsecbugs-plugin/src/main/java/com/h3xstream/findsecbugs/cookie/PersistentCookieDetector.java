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
package com.h3xstream.findsecbugs.cookie;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class PersistentCookieDetector extends OpcodeStackDetector {

    private BugReporter bugReporter;

    public PersistentCookieDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("javax/servlet/http/Cookie")
                && getNameConstantOperand().equals("setMaxAge")) {

            Object maxAge = stack.getStackItem(0).getConstant();
            Integer n = (maxAge instanceof Integer) ? (Integer)maxAge : 0;

            //Max age equal or greater than one year
            if (n >= 31536000) {
                bugReporter.reportBug(new BugInstance(this, "COOKIE_PERSISTENT", Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }
}