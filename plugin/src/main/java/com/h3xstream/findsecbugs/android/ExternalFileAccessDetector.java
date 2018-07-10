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

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

public class ExternalFileAccessDetector extends OpcodeStackDetector {

    private static final String ANDROID_EXTERNAL_FILE_ACCESS_TYPE = "ANDROID_EXTERNAL_FILE_ACCESS";
    private BugReporter bugReporter;

    public ExternalFileAccessDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

        // getClassConstantOperand().equals("java/net/Socket")

        if (seen == Const.INVOKEVIRTUAL && ( //List of method mark as external file access
                getNameConstantOperand().equals("getExternalCacheDir") ||
                getNameConstantOperand().equals("getExternalCacheDirs") ||
                getNameConstantOperand().equals("getExternalFilesDir") ||
                getNameConstantOperand().equals("getExternalFilesDirs") ||
                getNameConstantOperand().equals("getExternalMediaDirs")
            )) {
            bugReporter.reportBug(new BugInstance(this, ANDROID_EXTERNAL_FILE_ACCESS_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
        else if(seen == Const.INVOKESTATIC && getClassConstantOperand().equals("android/os/Environment") && (
                getNameConstantOperand().equals("getExternalStorageDirectory") ||
                getNameConstantOperand().equals("getExternalStoragePublicDirectory")
            )) {
            bugReporter.reportBug(new BugInstance(this, ANDROID_EXTERNAL_FILE_ACCESS_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
    }

}
