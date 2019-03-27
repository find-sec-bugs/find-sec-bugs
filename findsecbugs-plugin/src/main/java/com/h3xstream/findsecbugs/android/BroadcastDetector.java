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
package com.h3xstream.findsecbugs.android;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class BroadcastDetector extends OpcodeStackDetector {

    private static final String ANDROID_BROADCAST_TYPE = "ANDROID_BROADCAST";
    private BugReporter bugReporter;

    public BroadcastDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

        if (seen == Const.INVOKEVIRTUAL &&
                (
                    getNameConstantOperand().equals("sendBroadcast") ||
                    getNameConstantOperand().equals("sendBroadcastAsUser") ||
                    getNameConstantOperand().equals("sendOrderedBroadcast") ||
                    getNameConstantOperand().equals("sendOrderedBroadcastAsUser")
                )) {

            // The LocalBroadcastManager object is safe. The broadcast doesn't leave the application scope.
            // We check if the class extends android.support.v4.content.LocalBroadcastManager
            // We will also check if the class is named "LocalBroadcastManager" in case the version in the namespace changes.
            if (!InterfaceUtils.isSubtype(getClassConstantOperand(), "android.support.v4.content.LocalBroadcastManager")
                    && !getClassConstantOperand().endsWith("LocalBroadcastManager")) {
                bugReporter.reportBug(new BugInstance(this, ANDROID_BROADCAST_TYPE, Priorities.NORMAL_PRIORITY) //
                        .addClass(this).addMethod(this).addSourceLine(this));
            }
        }
    }
}
