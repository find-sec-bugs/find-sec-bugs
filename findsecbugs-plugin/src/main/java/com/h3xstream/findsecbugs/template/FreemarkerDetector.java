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

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;

/**
 * Equivalent to Velocity template detector.
 */
public class FreemarkerDetector extends OpcodeStackDetector {

    private static final String FREEMARKER_TYPE = "TEMPLATE_INJECTION_FREEMARKER";

    private BugReporter bugReporter;

    public FreemarkerDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        //printOpCode(seen);

//  FreemarkerDetector: [0113]  invokevirtual   freemarker/template/Template.process (Ljava/lang/Object;Ljava/io/Writer;)V

        if (seen == Const.INVOKEVIRTUAL && getClassConstantOperand().equals("freemarker/template/Template")
                && getNameConstantOperand().equals("process")) {

            bugReporter.reportBug(new BugInstance(this, FREEMARKER_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(this).addMethod(this).addSourceLine(this));
        }
    }
}
