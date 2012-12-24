/**
 * Find Security Bugs
 * Copyright (c) 2013, Philippe Arteau, All rights reserved.
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

/**
 * Few methods from <i>org.apache.commons.io.FilenameUtils</i> have a common weakness
 * of not filtering properly null byte.
 * <ul>
 * <li>normalize</li>
 * <li>getExtension</li>
 * <li>isExtension</li>
 * <li>getName</li>
 * <li>getBaseName</li>
 * </ul>
 *
 * <p>
 * In practice, it has limited risk see example in WeakFilenameUtils.
 *
 * @see org.apache.commons.io.FilenameUtils
 */
public class WeakFilenameUtilsMethodDetector extends OpcodeStackDetector {

    private static final String WEAK_FILENAMEUTILS_TYPE = "WEAK_FILENAMEUTILS";

    private BugReporter bugReporter;

    public WeakFilenameUtilsMethodDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Constants.INVOKESTATIC && getClassConstantOperand().equals("org/apache/commons/io/FilenameUtils") &&
                (getNameConstantOperand().equals("normalize") ||
                        getNameConstantOperand().equals("getExtension") ||
                        getNameConstantOperand().equals("isExtension") ||
                        getNameConstantOperand().equals("getName") ||
                        getNameConstantOperand().equals("getBaseName")
                )) {

            bugReporter.reportBug(new BugInstance(this, WEAK_FILENAMEUTILS_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString(getNameConstantOperand()));
        }
    }
}
