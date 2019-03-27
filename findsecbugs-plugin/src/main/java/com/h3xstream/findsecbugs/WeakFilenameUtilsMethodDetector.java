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

import com.h3xstream.findsecbugs.common.matcher.InvokeMatcherBuilder;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

import static com.h3xstream.findsecbugs.common.matcher.InstructionDSL.invokeInstruction;

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
 * </p>
 *
 */
public class WeakFilenameUtilsMethodDetector extends OpcodeStackDetector {

    private static final String WEAK_FILENAMEUTILS_TYPE = "WEAK_FILENAMEUTILS";

    private BugReporter bugReporter;

    private static final InvokeMatcherBuilder FILENAMEUTILS_NULL_METHOD = invokeInstruction().atClass("org/apache/commons/io/FilenameUtils")
            .atMethod("normalize","getExtension","isExtension","getName","getBaseName");

    public WeakFilenameUtilsMethodDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {

        if (seen == Const.INVOKESTATIC && FILENAMEUTILS_NULL_METHOD.matches(this)) {

            bugReporter.reportBug(new BugInstance(this, WEAK_FILENAMEUTILS_TYPE, Priorities.LOW_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this)
                    .addString(getNameConstantOperand()));
        }
    }
}
