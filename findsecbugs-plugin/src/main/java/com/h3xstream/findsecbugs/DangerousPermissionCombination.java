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

import org.apache.bcel.Const;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;

public class DangerousPermissionCombination extends OpcodeStackDetector {

    private BugReporter bugReporter;

    public DangerousPermissionCombination(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    private void reportBug(String permission, String target) {
        BugInstance bug = new BugInstance(this, "DANGEROUS_PERMISSION_COMBINATION", NORMAL_PRIORITY)
                .addClassAndMethod(this).addString(permission).addString(target).addSourceLine(this);
        bugReporter.reportBug(bug);
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Const.INVOKESPECIAL && Const.CONSTRUCTOR_NAME.equals(getNameConstantOperand())) {
            ClassDescriptor cd = getClassDescriptorOperand();
            if (cd != null && "Ljava/lang/reflect/ReflectPermission;".equals(cd.getSignature())) {
                String stringParam = (String) stack.getStackItem(0).getConstant();
                if (stringParam != null && "suppressAccessChecks".equals(stringParam)) {
                    reportBug("ReflectPermission", "suppressAccessChecks");
                }
            } else if (cd != null && "Ljava/lang/RuntimePermission;".equals(cd.getSignature())) {
                String stringParam = (String) stack.getStackItem(0).getConstant();
                if (stringParam != null && "createClassLoader".equals(stringParam)) {
                    reportBug("RuntimePermission", "createClassLoader");
                }
            }
        }
    }
}
